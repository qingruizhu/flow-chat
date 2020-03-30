package com.flow.chat.bgd.mapper;

import com.flow.chat.bgd.model.ChatContent;
import com.flow.chat.bgd.model.ChatContentExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChatContentMapper {
    long countByExample(ChatContentExample example);

    int deleteByExample(ChatContentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ChatContent record);

    int insertSelective(ChatContent record);

    List<ChatContent> selectByExample(ChatContentExample example);

    ChatContent selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ChatContent record, @Param("example") ChatContentExample example);

    int updateByExample(@Param("record") ChatContent record, @Param("example") ChatContentExample example);

    int updateByPrimaryKeySelective(ChatContent record);

    int updateByPrimaryKey(ChatContent record);
}