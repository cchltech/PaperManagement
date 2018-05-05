package com.cchl.dao;

import com.cchl.entity.Teacher;
import java.util.List;

public interface TeacherMapper {
    int insert(Teacher record);

    List<Teacher> selectAll();
}