package com.cchl.dao;

import com.cchl.entity.WeeksPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WeeksPlanMapper {

    int insert(WeeksPlan record);

    List<WeeksPlan> selectAll();

    int isExist(Integer paperId);

    int updateFilePath(@Param(value = "paperId") Integer paperId, @Param(value = "filePath") String filePath);
}