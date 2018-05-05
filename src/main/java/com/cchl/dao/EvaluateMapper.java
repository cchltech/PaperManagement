package com.cchl.dao;

import com.cchl.entity.Evaluate;
import java.util.List;

public interface EvaluateMapper {
    int insert(Evaluate record);

    List<Evaluate> selectAll();
}