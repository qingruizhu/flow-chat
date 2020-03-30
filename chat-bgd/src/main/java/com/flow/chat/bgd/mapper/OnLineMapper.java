package com.flow.chat.bgd.mapper;

import com.flow.chat.bgd.model.OnLine;
import com.flow.chat.bgd.model.OnLineExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OnLineMapper {
    long countByExample(OnLineExample example);

    int deleteByExample(OnLineExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OnLine record);

    int insertSelective(OnLine record);

    List<OnLine> selectByExample(OnLineExample example);

    OnLine selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OnLine record, @Param("example") OnLineExample example);

    int updateByExample(@Param("record") OnLine record, @Param("example") OnLineExample example);

    int updateByPrimaryKeySelective(OnLine record);

    int updateByPrimaryKey(OnLine record);
}