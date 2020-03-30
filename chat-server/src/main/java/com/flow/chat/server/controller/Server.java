package com.flow.chat.server.controller;


import com.flow.chat.bgd.model.OnLine;
import com.flow.chat.bgd.model.User;
import com.flow.chat.bgd.service.IOnlineService;
import com.flow.chat.common.DateformateUtil;
import com.flow.chat.common.Message;
import com.flow.chat.common.MessageType;
import com.flow.chat.server.model.ManagerClientThread;
import com.flow.chat.server.model.ServerConClientThread;
import com.flow.chat.server.service.ServerChatContentService;
import com.flow.chat.server.service.ServerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 * 聊天服务器：监听，等待客户端连接
 */
@Component
public class Server {

    @Autowired
    private ServerUserService userService;
    @Autowired
    private ServerChatContentService chatContentService;
    @Autowired
    private IOnlineService onlineService;

    public void listening() {
        try {
            System.out.println("服务器，在9999开始监听...");
            //监听
            ServerSocket server = new ServerSocket(9999);
            while (true) {
                //阻塞等待连接
                Socket accept = server.accept();
                ObjectInputStream ois = new ObjectInputStream(accept.getInputStream());
                User user = (User) ois.readObject();
                System.out.println(user.toString());
                Message message = new Message();
                ObjectOutputStream oos = new ObjectOutputStream(accept.getOutputStream());
                User select = userService.select(user);
                if (null != select && select.getPassword().equals(user.getPassword())) {
//                if ("123456".equals(user.getPassword())) {
                    /** 1.返回登录成功 */
                    message.setMesType(MessageType.message_succeed);
                    oos.writeObject(message);
                    /** 2.修改登录状态*/
//                    User update = new User();
//                    update.setId(select.getId());
//                    update.setOnline(1);
//                    userService.update(update);
                    OnLine onLine = new OnLine();
                    onLine.setUserId(select.getUserId());
                    onLine.setOnline(true);
                    onLine.setLoginTime(DateformateUtil.yMd(new Date()));
                    if (onlineService.update(onLine) <= 0) {
                        onlineService.insert(onLine);
                    }
                    /** 3.开启一个线程与客户端保持通讯 */
                    ServerConClientThread thread = new ServerConClientThread(accept);
                    thread.setOnlineService(onlineService);
                    thread.setChatContentService(chatContentService);
                    ManagerClientThread.set(user.getUserId(), thread);
                    thread.start();//启动与该客户端通讯的线程
                    /** 4.通知其好友自己已上线 */
                    List<User> users = userService.selectOnlineFriendsByUserId(user.getUserId());
                    if (null != users && users.size() > 0) {
                        Message ms = new Message();
                        ms.setCon(user.getUserId());
                        ms.setMesType(MessageType.message_ret_onLineFriend);
                        for (User friend : users) {
                            ServerConClientThread friendThread = ManagerClientThread.get(friend.getUserId());
                            if (null != friendThread && null != friendThread.getSocket()) {
                                ms.setGetter(friend.getUserId());
                                ObjectOutputStream outputStream = new ObjectOutputStream(friendThread.getSocket().getOutputStream());
                                outputStream.writeObject(ms);
                            }
                        }
                    }
                } else {
                    //失败->关闭连接
                    message.setMesType(MessageType.message_login_fail);
                    oos.writeObject(message);
                    accept.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
