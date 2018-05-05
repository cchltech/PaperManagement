package com.cchl.dao;

import com.cchl.entity.Major;
import java.util.List;

public interface MajorMapper {
    int insert(Major record);

    List<Major> selectAll();
}