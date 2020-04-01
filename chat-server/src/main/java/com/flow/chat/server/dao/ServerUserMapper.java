package com.flow.chat.server.dao;

import com.flow.chat.bgd.model.User;

import java.util.List;

public interface ServerUserMapper {
    /** 查询好友 */
    public List<User> selectFriendsByUserId(String userId);
    /** 查询在线好友 */
    public List<User> selectOnlineFriendsByUserId(String userId);
}
