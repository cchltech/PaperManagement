package com.cchl.service.teacher.Ipml;

import com.cchl.dao.TeacherMapper;
import com.cchl.dao.UserMapper;
import com.cchl.dto.Result;
import com.cchl.entity.Student;
import com.cchl.entity.Teacher;
import com.cchl.entity.User;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.DataInsertException;
import com.cchl.execption.SystemException;
import com.cchl.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 教师类处理登录和注册的请求
 */
@Service
@Qualifier(value = "teacher")
public class TeacherLoginServiceImpl implements LoginService {

    private Logger logger = LoggerFactory.getLogger(TeacherLoginServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    /**
     * 登录校验
     *
     * @param id       账号
     * @param password 密码
     * @return 返回结果，0：失败 1：成功
     */
    @Override
    public Integer loginCheck(String id, String password) {
        try {
            Long id1 = Long.parseLong(id);
            Integer userId = teacherMapper.loginCheck(id1, password);
            return userId == null ? 0 : userId;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 判断身份
     *
     * @param id 账号
     * @return 返回结果
     */
    public boolean isTeacher(Long id) {
        return teacherMapper.isTeacher(id) > 0;
    }

    @Override
    @Transactional
    public Result teacherRegister(Teacher teacher) {
        try {
            //先添加一个用户，类型为教师（1），状态为待审核（0）
            User user = new User();
            user.setType((byte) 1);
            user.setStatus((byte) 0);
            if (userMapper.insert(user) > 0) {
                teacher.setUserId(user.getId());
                if (teacherMapper.insert(teacher) > 0) {
                    logger.info("教师注册成功，教师实体为：{}", teacher.toString());
                    return new Result(Dictionary.SUCCESS);
                } else {
                    //如果数据插入失败，抛出异常，事务回滚
                    logger.error("教师信息插入失败，教师实体为：{}", teacher.toString());
                    throw new DataInsertException(Dictionary.DATA_INSERT_FAIL);
                }
            } else {
                //如果数据插入失败，抛出异常，事务回滚
                logger.error("用户信息插入失败，用户实体为：{}", user.toString());
                throw new DataInsertException(Dictionary.DATA_INSERT_FAIL);
            }
        } catch (Exception e) {
            //如果失败，抛出异常，事务回滚
            logger.error("系统出现异常，异常信息为：{}", e.getMessage());
            throw new SystemException(Dictionary.SYSTEM_ERROR);
        }
    }

    @Override
    public Result studentRegister(Student student) {
        return null;
    }

}
