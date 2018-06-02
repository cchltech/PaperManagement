package com.cchl.dao;

import com.cchl.entity.MidCheck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface MidCheckMapper {

    int insert(MidCheck record);

    List<MidCheck> selectAll();

    int isExist(Integer paperId);

    int updateFilePath(@Param(value = "paperId") Integer paperId, @Param(value = "filePath") String filePath);
}