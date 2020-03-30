package com.flow.chat.bgd.service;

import com.flow.chat.bgd.mapper.UserMapper;
import com.flow.chat.bgd.model.User;
import com.flow.chat.bgd.model.UserExample;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Resource
    private UserMapper mapper;

    @Override
    public User select(User select) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        if (null != select.getUserId() && !"".equals(select.getUserId())) {
            criteria.andUserIdEqualTo(select.getUserId());
        }
        List<User> users = mapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(users)) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public int update(User update) {
        if (null != update.getId()) {
            return mapper.updateByPrimaryKeySelective(update);
        }
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        if (null != update.getUserId() && !"".equals(update.getUserId())) {
            criteria.andUserIdEqualTo(update.getUserId());
        }
        return mapper.updateByExampleSelective(update, example);
    }


}
