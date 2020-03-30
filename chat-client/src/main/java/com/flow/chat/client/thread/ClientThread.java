package com.flow.chat.client.thread;

import com.flow.chat.client.controller.Chat;
import com.flow.chat.client.controller.FriendList;
import com.flow.chat.client.file.ReceiveFrame;
import com.flow.chat.client.util.ManagerChat;
import com.flow.chat.client.util.ManagerFriendList;
import com.flow.chat.common.Message;
import com.flow.chat.common.MessageType;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientThread extends Thread {

    private Socket socket;
    private ReceiveFrame receiveFrame;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //客户端从服务端读取信息,读不到就一直等待
                ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
                Message message = (Message) ois.readObject();
                if (message.getMesType().equals(MessageType.message_comm_mes)) {
                    /** 普通消息 */
                    //显示在聊天框中
                    Chat chat = ManagerChat.get(message.getGetter() + " " + message.getSender());
                    chat.showMessage(message);
                } else if (message.getMesType().equals(MessageType.message_ret_outlineMessage)) {
                    /** 离线信息 */
                    Chat chat = ManagerChat.get(message.getGetter() + " " + message.getSender());
                    chat.showMessage(message);
                } else if (message.getMesType().equals(MessageType.message_ret_onLineFriend)) {
                    /** 好友列表 */
                    String comeId = message.getSender();
                    System.out.println("好友上线通知：" + comeId);
                    FriendList friendList = ManagerFriendList.get(message.getGetter());
                    if (null != friendList) {
                        friendList.updateFriendList(comeId,true);
                    }
                } else if (message.getMesType().equals(MessageType.message_sendfile)) {
                    /** 接收文件 */
                    if (receiveFrame == null) {
                        receiveFrame = new ReceiveFrame();
                    }
                    receiveFrame.setMessage(message);
                    receiveFrame.setVisible(true);
//                    else {
//                        receiveFrame.setMs(message);
//                        receiveFrame.saveFile();
//                        receiveFrame.setVisible(false);
//                        receiveFrame = null;
//                    }
                } else if (message.getMesType().equals(MessageType.message_friend_login_out)) {
                    /** 好友下线通知 */
                    String outId = message.getSender();
                    System.out.println("好友下线通知：" + outId);
                    FriendList friendList = ManagerFriendList.get(message.getGetter());
                    if (null != friendList) {
                        friendList.updateFriendList(outId,false);
                    }
                }else if (message.getMesType().equals(MessageType.message_login_out)) {
                    /** 成功退出 */
                    //终止线程
                    this.interrupt();
                    if (this.isInterrupted()) {
                        System.exit(0);
                    }
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
