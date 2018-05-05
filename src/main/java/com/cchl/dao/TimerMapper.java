package com.cchl.dao;

import com.cchl.entity.Timer;
import java.util.List;

public interface TimerMapper {
    int insert(Timer record);

    List<Timer> selectAll();
}