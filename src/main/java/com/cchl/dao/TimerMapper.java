package com.cchl.dao;

import com.cchl.entity.Timer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface TimerMapper {

    int insert(Timer record);

    List<Timer> selectAll();
}