package com.cchl.dao;

import com.cchl.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface TeacherMapper {

    int insert(Teacher record);

    List<Teacher> selectAll();
}