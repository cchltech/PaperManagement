package com.cchl.dao;

import com.cchl.entity.UserPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserPaperMapper {

    int insert(UserPaper userPaper);

    List<Integer> selectByUserId(int userId);
}
