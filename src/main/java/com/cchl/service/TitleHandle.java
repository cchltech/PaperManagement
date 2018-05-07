package com.cchl.service;

import com.cchl.dao.TitleMapper;
import com.cchl.entity.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TitleHandle {

    @Autowired
    private TitleMapper titleMapper;

    public int insert(Title title) {
        return titleMapper.insert(title);
    }
}
