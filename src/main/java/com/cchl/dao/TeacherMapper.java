package com.cchl.dao;

import com.cchl.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeacherMapper {

    int insert(Teacher teacher);

    List<Teacher> selectAll();

    Integer loginCheck(@Param(value = "id") Long id, @Param(value = "password") String password);

    int isTeacher(Long id);

    Integer selectDepartmentIdByUserId(int userId);
}