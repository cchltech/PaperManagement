package com.cchl.dao;

import com.cchl.entity.AdminInfo;
import java.util.List;

public interface AdminInfoMapper {
    int insert(AdminInfo record);

    List<AdminInfo> selectAll();
}