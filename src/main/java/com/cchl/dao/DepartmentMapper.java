package com.cchl.dao;

import com.cchl.entity.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface DepartmentMapper {

    int insert(Department record);

    List<Department> selectAll();
}