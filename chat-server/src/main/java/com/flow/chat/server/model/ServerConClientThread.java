package com.flow.chat.server.model;

import com.flow.chat.bgd.model.ChatContent;
import com.flow.chat.bgd.model.OnLine;
import com.flow.chat.bgd.service.IOnlineService;
import com.flow.chat.common.DateformateUtil;
import com.flow.chat.common.Message;
import com.flow.chat.common.MessageType;
import com.flow.chat.server.service.ServerChatContentService;

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
    private IOnlineService onlineService;
    private ServerChatContentService chatContentService;

    public ServerConClientThread(Socket socket) {
        this.socket = socket; //把socket给该线程
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
                    chatContentService.insert(chatContent);
                    //转发
                    ServerConClientThread getterThread = ManagerClientThread.get(message.getGetter());
                    if (null != getterThread) {
                        ObjectOutputStream oos = new ObjectOutputStream(getterThread.socket.getOutputStream());
                        oos.writeObject(message);
                    }
                } else if (message.getMesType().equals(MessageType.message_ret_outlineMessage)) {
                    /** 2.获取离线信息*/
                    List<ChatContent> chatContents = chatContentService.selectOutlineChatContent(message.getGetter(), message.getSender());
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
                    onlineService.update(onLine);
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

    public void setChatContentService(ServerChatContentService chatContentService) {
        this.chatContentService = chatContentService;
    }

    public void setOnlineService(IOnlineService onlineService) {
        this.onlineService = onlineService;
    }
}
