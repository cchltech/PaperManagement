package com.cchl.dao;

import com.cchl.entity.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface StudentMapper {

    int insert(Student record);

    List<Student> selectAll();
}