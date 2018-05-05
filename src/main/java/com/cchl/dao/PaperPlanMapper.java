package com.cchl.dao;

import com.cchl.entity.PaperPlan;
import java.util.List;

public interface PaperPlanMapper {
    int insert(PaperPlan record);

    List<PaperPlan> selectAll();
}