package com.cchl.dao;

import com.cchl.entity.UserPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserPaperMapper {

    int insert(UserPaper userPaper);
}
