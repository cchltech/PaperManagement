package com.cchl.web;

import com.cchl.dto.Result;
import com.cchl.entity.Student;
import com.cchl.entity.Teacher;
import com.cchl.eumn.Dictionary;
import com.cchl.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 注册与登录控制
 */
@RestController
public class LoginController {

    @Autowired
    @Qualifier(value = "student")
    private LoginService studentLoginService;

    @Autowired
    @Qualifier(value = "teacher")
    private LoginService teacherLoginService;

    @Autowired
    @Qualifier(value = "admin")
    private LoginService adminLoginService;

    //日志记录
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * @return 跳转到初始页面
     */
    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    /**
     * @return 跳转到注册页面
     */
    @RequestMapping(value = "/register")
    public String register() {
        return "register";
    }

    /**
     * 学生注册
     *
     * @param student 学生实体类
     * @return 返回结果
     */
    @RequestMapping(value = "/studentRegister", method = RequestMethod.POST)
    public Result studentRegister(@RequestBody(required = false) Student student) {
        try {
            //验证信息是否有缺失
            if (student != null
                    && student.getId() != null && student.getName() != null
                    && student.getPassword() != null && student.getSex() != null
                    && student.getDepartmentId() != null && student.getGrade() != null
                    && student.getMajorId() != null && student.getPhone() != null) {
                logger.info("注册成功");
                //执行注册操作
                return studentLoginService.studentRegister(student);
            } else {
                logger.info("缺少必要信息");
                return new Result(Dictionary.DATA_LOST);
            }
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 教师注册
     * @param teacher 教师实体类
     * @return 返回结果
     */
    @RequestMapping(value = "/teacherRegister", method = RequestMethod.POST)
    public Result teacherRegister(@RequestBody(required = false) Teacher teacher) {
        try {
            if (teacher != null
                    && teacher.getId() != null && teacher.getName() != null
                    && teacher.getPassword() != null && teacher.getSex() != null
                    && teacher.getDepartmentId() != null && teacher.getPhone() != null) {
                logger.info("注册成功");
                //执行注册操作
                return teacherLoginService.teacherRegister(teacher);
            } else {
                logger.info("缺少必要信息");
                return new Result(Dictionary.DATA_LOST);
            }
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }


    /**
     * 处理登录请求
     *
     * @param id       学号或者是教师工号
     * @param password 密码
     * @param type     选择身份
     * @return 视图
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam(value = "id") String id, @RequestParam(value = "password") String password, @RequestParam(value = "type") String type, HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        HttpSession session = request.getSession();
        try {
            /*
             * 判断登录身份
             * 成功之后将学号/工号，用户id存进session
             */
            int userId;
            if ("student".equals(type)) {
                userId = studentLoginService.loginCheck(id, password);
                if (userId > 0) {
                    model.setViewName("student");
                    session.setAttribute("id", id);
                    session.setAttribute("user_id", userId);
                }
            } else if ("teacher".equals(type)) {
                userId = teacherLoginService.loginCheck(id, password);
                if (userId > 0) {
                    model.setViewName("teacher");
                    session.setAttribute("id", id);
                    session.setAttribute("user_id", userId);
                }
            } else if ("admin".equals(type)) {
                userId = adminLoginService.loginCheck(id, password);
                if (userId > 0) {
                    model.setViewName("admin");
                    session.setAttribute("id", id);
                    session.setAttribute("user_id", userId);
                }
            } else {
                logger.error("未知身份");
                model.setViewName("login");
                model.addObject("error", "账号或密码错误");
            }
            return model;
        } catch (Exception e) {
            logger.error("系统异常");
            model.setViewName("error");
            return model;
        }
    }
}
