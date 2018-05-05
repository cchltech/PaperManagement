package com.cchl.dao;

import com.cchl.entity.AdminInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminInfoMapper {

    int insert(AdminInfo record);

    List<AdminInfo> selectAll();
}