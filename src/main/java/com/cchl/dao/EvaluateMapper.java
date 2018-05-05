package com.cchl.dao;

import com.cchl.entity.Evaluate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface EvaluateMapper {

    int insert(Evaluate record);

    List<Evaluate> selectAll();
}