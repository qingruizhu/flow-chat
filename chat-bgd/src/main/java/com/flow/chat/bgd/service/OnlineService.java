package com.flow.chat.bgd.service;

import com.flow.chat.bgd.mapper.OnLineMapper;
import com.flow.chat.bgd.model.OnLine;
import com.flow.chat.bgd.model.OnLineExample;
import com.flow.chat.common.DateformateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public class OnlineService implements IOnlineService {

    @Resource
    private OnLineMapper mapper;

    @Override
    public List<OnLine> select(OnLine select) {
        OnLineExample example = new OnLineExample();
        OnLineExample.Criteria criteria = example.createCriteria();
        if (null != select.getUserId() && !"".equals(select.getUserId())) {
            criteria.andUserIdEqualTo(select.getUserId());
        }
        if (null != select.getOnline()) {
            criteria.andOnlineEqualTo(select.getOnline());
        }
        example.setOrderByClause("time desc");
        if (criteria.isValid()) {
            return mapper.selectByExample(example);
        }
        return null;
    }

    @Override
    public int insert(OnLine insert) {
        if (null == insert.getLoginTime() && "".equals(insert.getLoginTime())) {
            insert.setLoginTime(DateformateUtil.yMd(new Date()));
        }
        return mapper.insertSelective(insert);
    }

    @Override
    public int update(OnLine update) {
        if (null == update.getUserId() || "".equals(update.getUserId())) {
            return 0;
        }
        OnLineExample example = new OnLineExample();
        OnLineExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(update.getUserId());
        return mapper.updateByExampleSelective(update, example);
    }


}
