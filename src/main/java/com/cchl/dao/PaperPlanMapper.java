package com.cchl.dao;

import com.cchl.entity.PaperPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface PaperPlanMapper {

    int insert(PaperPlan record);

    List<PaperPlan> selectAll();

    int insertTitle(@Param("userId") int userId, @Param("titleId")int titleId);

    Integer selectByTitleId(Integer titleId);


}