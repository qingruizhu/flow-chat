package com.flow.chat.bgd.service;

import com.flow.chat.bgd.mapper.ChatContentMapper;
import com.flow.chat.bgd.model.ChatContent;
import com.flow.chat.bgd.model.ChatContentExample;
import com.flow.chat.common.DateformateUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public class ChatContentService implements IChatContentService {

    @Resource
    private ChatContentMapper mapper;

    @Override
    public List<ChatContent> select(ChatContent content) {
        ChatContentExample example = new ChatContentExample();
        ChatContentExample.Criteria criteria = example.createCriteria();
        if (null != content.getSender() && !"".equals(content.getSender())) {
            criteria.andSenderEqualTo(content.getSender());
        }

        if (null != content.getGetter() && !"".equals(content.getGetter())) {
            criteria.andGetterEqualTo(content.getGetter());
        }

        if (null != content.getSendTime() && !"".equals(content.getSendTime())) {
            criteria.andSendTimeGreaterThan(content.getSendTime());
        }

        if (criteria.isValid()) {
            return mapper.selectByExample(example);
        }
        return null;
    }

    @Override
    public int insert(ChatContent content) {
        if (null == content.getSendTime()) {
            content.setSendTime(DateformateUtil.yMd(new Date()));
        }
        return mapper.insertSelective(content);
    }


}
