package com.flow.chat.bgd.service;

import com.flow.chat.bgd.model.OnLine;

import java.util.List;

public interface IOnlineService {
    List<OnLine> select(OnLine select);

    int insert(OnLine insert);

    int update(OnLine update);
}
