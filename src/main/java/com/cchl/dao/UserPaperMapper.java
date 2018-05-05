package com.cchl.dao;

import com.cchl.entity.UserPaper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserPaperMapper {

    int insert(UserPaper userPaper);

}
