package com.cchl.dao;

import com.cchl.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface StudentMapper {

    int insert(Student student);

    List<Student> selectAll();

    Student selectById(Long id);

    Integer loginCheck(@Param("id")Long id, @Param("password")String password);

    List<Student> selectUnTitle();

    Student selectByUserId(Integer userId);


    int updateEmail(@Param(value = "email") String email, @Param(value = "id") Long id);

    int updatePhone(@Param(value = "phone") Long phone, @Param(value = "id") Long id);

    int totalNumber(Integer departmentId);

    List<Student> selectByDepartmentId(Integer departmentId);

}