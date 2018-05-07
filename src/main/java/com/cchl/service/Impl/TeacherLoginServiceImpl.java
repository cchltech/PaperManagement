package com.cchl.service.Impl;

import com.cchl.dto.Result;
import com.cchl.entity.Student;
import com.cchl.entity.Teacher;
import com.cchl.service.LoginService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 教师类处理登录和注册的请求
 */
@Service
@Qualifier(value = "teacher")
public class TeacherLoginServiceImpl implements LoginService {

    @Override
    public int loginCheck(String id, String password) {
        return 0;
    }

    @Override
    public Result studentRegister(Student student) {
        return null;
    }

    @Override
    public Result teacherRegister(Teacher teacher) {
        return null;
    }

}
