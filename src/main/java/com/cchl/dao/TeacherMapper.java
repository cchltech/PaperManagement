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

    List<Teacher> selectByStatus(@Param(value = "page") int page, @Param(value = "limit") int limit, @Param(value = "status") int status, @Param(value = "all") boolean all, @Param(value = "departmentId") int departmentId);

    int totalNumber(@Param(value = "all") boolean all, @Param(value = "status") byte status, @Param(value = "departmentId") int departmentId);
}