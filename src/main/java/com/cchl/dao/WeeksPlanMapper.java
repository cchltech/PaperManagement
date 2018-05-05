package com.cchl.dao;

import com.cchl.entity.WeeksPlan;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface WeeksPlanMapper {

    int insert(WeeksPlan record);

    List<WeeksPlan> selectAll();
}