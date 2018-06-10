package com.cchl.dao;

import com.cchl.entity.Admin;
import org.apache.ibatis.annotations.Param;

public interface AdminMapper {

    Integer loginCheck(@Param(value = "userId") Integer userId, @Param(value = "password")String password);

    Admin selectById(Integer userId);
}
