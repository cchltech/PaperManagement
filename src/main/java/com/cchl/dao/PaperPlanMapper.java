package com.cchl.dao;

import com.cchl.entity.PaperPlan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface PaperPlanMapper {

    int insert(PaperPlan record);

    List<PaperPlan> selectAll();
}