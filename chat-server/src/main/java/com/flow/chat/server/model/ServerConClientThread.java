package com.flow.chat.server.model;

import com.flow.chat.bgd.model.ChatContent;
import com.flow.chat.bgd.model.OnLine;
import com.flow.chat.bgd.model.User;
import com.flow.chat.common.DateformateUtil;
import com.flow.chat.common.Message;
import com.flow.chat.common.MessageType;
import com.flow.chat.server.component.TableService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 * 服务器与某个客户端的通讯线程
 */
public class ServerConClientThread extends Thread {
    private Socket socket;
    private TableService service;

    public ServerConClientThread(Socket socket, TableService service) {
        this.socket = socket; //把socket给该线程
        this.service = service;
    }

    @Override
    public void run() {
        ChatContent chatContent = new ChatContent();
        while (true) {
            try {
                /** 该线程接收客户端的信息 */
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if (message.getMesType().equals(MessageType.message_comm_mes)) {
                    /** 1.普通消息转发 */
                    System.out.println(message.getSender() + " 给 " + message.getGetter() + " 说：" + message.getCon());
                    //消息入库
                    chatContent.setSender(message.getSender());
                    chatContent.setGetter(message.getGetter());
                    chatContent.setSendTime(DateformateUtil.yMd(new Date()));
                    chatContent.setContent(message.getCon());
                    service.insert(chatContent);
                    //转发
                    ServerConClientThread getterThread = ManagerClientThread.get(message.getGetter());
                    if (null != getterThread) {
                        ObjectOutputStream oos = new ObjectOutputStream(getterThread.socket.getOutputStream());
                        oos.writeObject(message);
                    }
                } else if (message.getMesType().equals(MessageType.message_ret_outlineMessage)) {
                    /** 2.获取离线信息*/
                    List<ChatContent> chatContents = service.selectOutlineChatContent(message.getGetter(), message.getSender());
                    if (null != chatContents && chatContents.size() > 0) {
                        for (ChatContent content : chatContents) {
                            ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
                            Message retMsg = new Message();
                            retMsg.setSender(content.getSender());
                            retMsg.setGetter(content.getGetter());
                            retMsg.setSendTime(content.getSendTime());
                            retMsg.setCon(content.getContent());
                            retMsg.setMesType(MessageType.message_ret_outlineMessage);
                            oos.writeObject(retMsg);
                        }
                    }
                } else if (message.getMesType().equals(MessageType.message_login_out)) {
                    /** 3.退出 -> 修改登录状态*/
                    OnLine onLine = new OnLine();
                    onLine.setUserId(message.getSender());
                    onLine.setOnline(false);
                    onLine.setLogoutTime(DateformateUtil.yMd(new Date()));
                    service.update(onLine);
                    //通知其好友自己已下线
                    List<User> users = service.selectOnlineFriends(message.getSender());
                    if (null != users && users.size() > 0) {
                        Message ms = new Message();
                        ms.setSender(message.getSender());
                        ms.setMesType(MessageType.message_friend_login_out);
                        for (User friend : users) {
                            ServerConClientThread friendThread = ManagerClientThread.get(friend.getUserId());
                            if (null != friendThread && null != friendThread.getSocket()) {
                                ms.setGetter(friend.getUserId());
                                ObjectOutputStream outputStream = new ObjectOutputStream(friendThread.getSocket().getOutputStream());
                                outputStream.writeObject(ms);
                            }
                        }
                    }
                    //终止线程
                    ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
                    oos.writeObject(message);
                    oos.close();
                    this.interrupt();
                    ManagerClientThread.remove(message.getSender());
                    break;
                } else if (message.getMesType().equals(MessageType.message_sendfile)) {
                    /** 4.发送文件 */
                    ServerConClientThread thread = ManagerClientThread
                            .get(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(
                            thread.getSocket().getOutputStream());
                    oos.writeObject(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

        }
    }

    public Socket getSocket() {
        return socket;
    }

}
