package com.flow.chat.server.service;

import com.flow.chat.bgd.model.User;
import com.flow.chat.bgd.service.UserService;
import com.flow.chat.server.dao.ServerUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Auther: qingruizhu
 * @Date: 2020/3/24 14:28
 */
@Service
public class ServerUserService extends UserService {

    @Resource
    private ServerUserMapper serverUserMapper;

    /**
     * 查询好友列表
     */
    public List<User> selectFriendsByUserId(String userId) {
        return serverUserMapper.selectFriendsByUserId(userId);
    }
    /**
     * 查询在线好友列表
     */
    public List<User> selectOnlineFriendsByUserId(String userId) {
        return serverUserMapper.selectOnlineFriendsByUserId(userId);
    }
}
