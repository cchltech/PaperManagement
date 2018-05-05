package com.cchl.dao;

import com.cchl.entity.WeeksPlan;
import java.util.List;

public interface WeeksPlanMapper {
    int insert(WeeksPlan record);

    List<WeeksPlan> selectAll();
}