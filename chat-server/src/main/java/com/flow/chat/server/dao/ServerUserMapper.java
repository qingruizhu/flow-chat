package com.flow.chat.server.dao;

import com.flow.chat.bgd.model.User;

import java.util.List;

public interface ServerUserMapper {
    public List<User> selectOnlineFriendsByUserId(String userId);
}
