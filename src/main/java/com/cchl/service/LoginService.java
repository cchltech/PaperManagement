package com.cchl.service;

import com.cchl.dto.Result;
import com.cchl.entity.Student;
import com.cchl.entity.Teacher;

public interface LoginService {
    //执行登录检查操作
    Integer loginCheck(String id, String password);
    //学生注册
    Result studentRegister(Student student);
    //教师注册
    Result teacherRegister(Teacher teacher);
}
