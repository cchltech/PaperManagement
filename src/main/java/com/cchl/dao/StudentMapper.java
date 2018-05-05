package com.cchl.dao;

import com.cchl.entity.Student;
import java.util.List;

public interface StudentMapper {
    int insert(Student record);

    List<Student> selectAll();
}