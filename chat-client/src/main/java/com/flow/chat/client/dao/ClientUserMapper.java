package com.flow.chat.client.dao;

import com.flow.chat.bgd.model.User;

import java.util.List;

public interface ClientUserMapper {
    public List<User> selectFriendsByUserId(String userId);
}
