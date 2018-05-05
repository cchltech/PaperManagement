package com.cchl.dao;

import com.cchl.entity.Department;
import java.util.List;

public interface DepartmentMapper {
    int insert(Department record);

    List<Department> selectAll();
}