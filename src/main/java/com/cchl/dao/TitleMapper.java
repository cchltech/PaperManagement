package com.cchl.dao;

import com.cchl.entity.Title;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface TitleMapper {

    int insert(Title record);

    List<Title> selectAll();
}