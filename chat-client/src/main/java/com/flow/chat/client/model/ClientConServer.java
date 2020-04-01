package com.flow.chat.client.model;

import com.flow.chat.bgd.model.User;
import com.flow.chat.client.controller.FriendList;
import com.flow.chat.client.thread.ClientThread;
import com.flow.chat.client.util.ManagerClientThread;
import com.flow.chat.client.util.ManagerFriendList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

@Component
public class ClientConServer {
    private Logger LOGGER = LoggerFactory.getLogger(ClientConServer.class);

    public boolean sendLoginInfoToServer(User user, String ip) {
//        List<User> aa = clientUserMapper.selectFriendsByUserId(user.getUserId());
//        System.out.println(aa.get(0));
        boolean b = false;
        try {
            Socket socket = new Socket(ip, 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            List<User> friends = (List<User>) ois.readObject();
//            if (message.getMesType().equals("1")) {//登录成功
            if (!CollectionUtils.isEmpty(friends)) {//登录成功
//                User me = userService.select(user);
                //创建好友列表
//                List<User> friends = clientUserMapper.selectFriendsByUserId(user.getUserId());
                User me = friends.get(0);
                friends.remove(0);
                FriendList friendList = new FriendList(me, friends);
                ManagerFriendList.set(user.getUserId(), friendList);
                //创建一个该qq号和服务器端保持通讯连接得线程
                ClientThread clientThread = new ClientThread(socket);
                clientThread.start();
                ManagerClientThread.set(user.getUserId(), clientThread);
                b = true;
            }
        } catch (Exception e) {
            LOGGER.error("请求登录失败!", e);
        }
        return b;
    }
}
