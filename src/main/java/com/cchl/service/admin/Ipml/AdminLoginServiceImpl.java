package com.cchl.service.admin.Ipml;

import com.cchl.dao.AdminMapper;
import com.cchl.dto.Result;
import com.cchl.entity.Student;
import com.cchl.entity.Teacher;
import com.cchl.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier(value = "admin")
public class AdminLoginServiceImpl implements LoginService{

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Integer loginCheck(String id, String password) {
        try {
            int userId = Integer.parseInt(id);
            Integer type = adminMapper.loginCheck(userId, password);
            return type == null ? 0 : type;
        } catch (NumberFormatException e) {
            return 0;
        }
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
