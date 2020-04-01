package com.flow.chat.server.controller;


import com.flow.chat.bgd.model.OnLine;
import com.flow.chat.bgd.model.User;
import com.flow.chat.common.DateformateUtil;
import com.flow.chat.common.Message;
import com.flow.chat.common.MessageType;
import com.flow.chat.server.model.ManagerClientThread;
import com.flow.chat.server.model.ServerConClientThread;
import com.flow.chat.server.component.TableService;
import com.flow.chat.server.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 聊天服务器：监听，等待客户端连接
 */
public class Server extends Thread{
    private Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private boolean on  = false;

    private TableService service;

    public Server(TableService service) {
        this.service = service;
    }

    public void listening() {
        if (this.on) {
            LOGGER.info("服务器已经在9999开始监听...");
            return;
        }
        LOGGER.info("服务器，开始9999开始监听...");
        try {
            //监听
            ServerSocket server = new ServerSocket(9999);
            this.on = true;
            while (this.on) {
                //阻塞等待连接
                Socket accept = server.accept();
                ObjectInputStream ois = new ObjectInputStream(accept.getInputStream());
                User user = (User) ois.readObject();
                LOGGER.info("{} -> 请求登录...", user.getUserId());
                Message message = new Message();
                ObjectOutputStream oos = new ObjectOutputStream(accept.getOutputStream());
                User select = service.select(user);
                if (null != select && select.getPassword().equals(user.getPassword())) {
                    /** 1.登录成功 -> 返回好友列表 */
                    List<User> friends = service.selectFriends(select.getUserId());
                    friends.add(0, select);//添加自己
                    oos.writeObject(friends);
                    /** 2.修改登录状态*/
//                    User update = new User();
//                    update.setId(select.getId());
//                    update.setOnline(1);
//                    userService.update(update);
                    OnLine onLine = new OnLine();
                    onLine.setUserId(select.getUserId());
                    onLine.setOnline(true);
                    onLine.setLoginTime(DateformateUtil.yMd(new Date()));
                    if (service.update(onLine) <= 0) {
                        service.insert(onLine);
                    }
                    /** 3.开启一个线程与客户端保持通讯 */
                    ServerConClientThread thread = new ServerConClientThread(accept, service);
                    ManagerClientThread.set(user.getUserId(), thread);
                    thread.start();//启动与该客户端通讯的线程
                    /** 4.通知其好友自己已上线 */
                    List<User> onUsers = service.selectOnlineFriends(user.getUserId());
                    if (null != onUsers && onUsers.size() > 0) {
                        Message ms = new Message();
                        ms.setSender(user.getUserId());
                        ms.setMesType(MessageType.message_ret_onLineFriend);
                        for (User friend : onUsers) {
                            ServerConClientThread friendThread = ManagerClientThread.get(friend.getUserId());
                            if (null != friendThread && null != friendThread.getSocket()) {
                                ms.setGetter(friend.getUserId());
                                ObjectOutputStream outputStream = new ObjectOutputStream(friendThread.getSocket().getOutputStream());
                                outputStream.writeObject(ms);
                            }
                        }
                    }
                    LOGGER.info("{} -> 登录成功!", user.getUserId());
                } else {
                    //失败->关闭连接
                    oos.writeObject(null);
                    accept.close();
                    LOGGER.info("{} -> 登录失败 !", user.getUserId());
                }
            }
        } catch (Exception e) {
            LOGGER.error("服务器异常", e);
        }
    }


    public void setOn(boolean on) {
        this.on = on;
    }

    @Override
    public void run() {
        this.listening();
    }
}
