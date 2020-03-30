package com.flow.chat.bgd.service;

import com.flow.chat.bgd.model.ChatContent;

import java.util.List;

public interface IChatContentService {
    List<ChatContent> select(ChatContent content);

    int insert(ChatContent content);
}
