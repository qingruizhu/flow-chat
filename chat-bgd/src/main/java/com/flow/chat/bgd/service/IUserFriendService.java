package com.flow.chat.bgd.service;

import com.flow.chat.bgd.model.UserFriend;

import java.util.List;

public interface IUserFriendService {
    public List<UserFriend> select(UserFriend select);
}
