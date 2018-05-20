package com.cchl.service.admin.Ipml;

import com.cchl.dto.Result;
import com.cchl.entity.Student;
import com.cchl.entity.Teacher;
import com.cchl.service.LoginService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier(value = "admin")
public class AdminLoginServiceImpl implements LoginService{
    @Override
    public Integer loginCheck(String id, String password) {
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
