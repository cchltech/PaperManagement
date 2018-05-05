package com.cchl.dao;

import com.cchl.entity.Major;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MajorMapper {

    int insert(Major record);

    List<Major> selectAll();
}