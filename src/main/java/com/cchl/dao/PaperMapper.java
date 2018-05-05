package com.cchl.dao;

import com.cchl.entity.Paper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface PaperMapper {

    int insert(Paper record);

    List<Paper> selectAll();
}