package com.flow.chat.server.component;

import com.flow.chat.bgd.model.ChatContent;
import com.flow.chat.bgd.model.OnLine;
import com.flow.chat.bgd.model.User;
import com.flow.chat.bgd.service.IOnlineService;
import com.flow.chat.server.service.ServerChatContentService;
import com.flow.chat.server.service.ServerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:
 * @Auther: qingruizhu
 * @Date: 2020/3/30 20:47
 */
@Component
public class TableService {

    @Autowired
    private ServerUserService userService;
    @Autowired
    private ServerChatContentService chatContentService;
    @Autowired
    private IOnlineService onlineService;

    public User select(User select) {
        return userService.select(select);
    }
    public List<User> selectOnlineFriends(String userId) {
        return userService.selectOnlineFriendsByUserId(userId);
    }
    public int update(OnLine update){
        return onlineService.update(update);
    }
    public int insert(OnLine insert){
        return onlineService.insert(insert);
    }
    public List<ChatContent> selectOutlineChatContent(String userId, String sender){
        return chatContentService.selectOutlineChatContent(userId,sender);
    }
    public int insert(ChatContent content){
        return chatContentService.insert(content);
    }
}
