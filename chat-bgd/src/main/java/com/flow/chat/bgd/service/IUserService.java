package com.flow.chat.bgd.service;

import com.flow.chat.bgd.model.User;

public interface IUserService {
    public User select(User select);

    public int update(User update);
}
