package com.flow.chat.server.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ManagerClientThread {

    private static Map<String, ServerConClientThread> manager = new HashMap<String, ServerConClientThread>();

    //添加客户端通讯线程
    public static void set(String userId, ServerConClientThread clientThread) {
        manager.put(userId, clientThread);
    }

    //获取客户端通讯线程
    public static ServerConClientThread get(String userId) {
        return manager.get(userId);
    }

    //移除客户端通讯线程
    public static ServerConClientThread remove(String userId) {
        return manager.remove(userId);
    }

    /**
     * 返回当前在线的人的id
     */
    public String onlineUserIds() {
        Iterator iterator = manager.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            sb.append(iterator.next() + " ");
        }
        return sb.toString();
    }
}
