package com.flow.chat.client.util;

import com.flow.chat.client.controller.Chat;

import java.util.HashMap;
import java.util.Map;

public class ManagerChat {

    private static Map<String, Chat> chats = new HashMap<String, Chat>();

    public static void set(String meAndFriendId, Chat chat) {
        chats.put(meAndFriendId, chat);
    }

    public static Chat get(String meAndFriendId) {
        return chats.get(meAndFriendId);
    }

    public static void remove(String meAndFriendId) {
        chats.remove(meAndFriendId);
    }
}
