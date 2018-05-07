package com.cchl.service.Impl;

import com.cchl.dao.StudentMapper;
import com.cchl.dao.UserMapper;
import com.cchl.dto.Result;
import com.cchl.entity.Student;
import com.cchl.entity.Teacher;
import com.cchl.entity.User;
import com.cchl.eumn.Dictionary;
import com.cchl.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学生类处理登录与注册的请求
 */
@Service
@Qualifier(value = "student")
public class StudentLoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public int loginCheck(String id, String password) {
        try{
            Long id1 = Long.parseLong(id);
            return studentMapper.loginCheck(id1, password);
        } catch (Exception e){
            e.getMessage();
            return 0;
        }

    }

    @Override
    @Transactional
    public Result studentRegister(Student student) {
        //注册学生前先添加一个user信息，类型为学生（0），状态为待审核（0）
        User user = new User();
        user.setStatus((byte)0);
        user.setType((byte)0);
        if (userMapper.insert(user) > 0) {
            //如果插入成功
            //获取数据库返回的主键值
            student.setUserId(user.getId());
            if (studentMapper.insert(student) > 0){
                return new Result(Dictionary.SUCCESS);
            } else {
                return new Result(Dictionary.SYSTEM_ERROR);
            }
        } else {
            //插入失败，返回失败信息
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    @Override
    public Result teacherRegister(Teacher teacher) {
        return null;
    }

}
