package com.flow.chat.bgd.service;

import com.flow.chat.bgd.mapper.UserFriendMapper;
import com.flow.chat.bgd.model.UserFriend;
import com.flow.chat.bgd.model.UserFriendExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

public class UserFriendService implements IUserFriendService {

    @Resource
    private UserFriendMapper mapper;

    @Override
    public List<UserFriend> select(UserFriend select) {
        UserFriendExample example = new UserFriendExample();
        UserFriendExample.Criteria criteria = example.createCriteria();
        if (null != select.getUserId() && !"".equals(select.getUserId())) {
            criteria.andUserIdEqualTo(select.getUserId());
        }
        List<UserFriend> friends = mapper.selectByExample(example);
        return friends;
    }

}
