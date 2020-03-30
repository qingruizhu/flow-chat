package com.flow.chat.server.dao;

import com.flow.chat.bgd.model.ChatContent;

import java.util.List;

public interface ServerChatContentMapper {
    public List<ChatContent> selectOutlineChatContent(String userId, String sender);
}
