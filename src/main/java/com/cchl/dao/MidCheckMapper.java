package com.cchl.dao;

import com.cchl.entity.MidCheck;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MidCheckMapper {

    int insert(MidCheck record);

    List<MidCheck> selectAll();
}