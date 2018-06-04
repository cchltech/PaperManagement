package com.cchl.web;

import com.cchl.dto.Result;
import com.cchl.entity.Student;
import com.cchl.entity.Teacher;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.DataInsertException;
import com.cchl.service.LoginService;
import com.cchl.service.student.StudentHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.zip.DataFormatException;

/**
 * 注册与登录控制
 */
@Controller
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

    @Autowired
    private StudentHandle studentHandle;

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
    @RequestMapping(value = "/registry")
    public String register() {
        return "registry";
    }

    /**
     * 学生注册
     * @return 返回结果
     */
    @RequestMapping(value = "/studentRegister", method = RequestMethod.POST)
    @ResponseBody
    public Result studentRegister(HttpServletRequest request) {
        try {
            //获取学生实体信息
            Student student = new Student();
            Map<String, String[]> map = request.getParameterMap();
            student.setId(Long.parseLong(map.get("id")[0]));
            student.setName(map.get("name")[0]);
            student.setPassword(map.get("password")[0]);
            student.setSex(Byte.valueOf(map.get("sex")[0]));
            student.setEmail(map.get("email")[0]);
            student.setDepartmentId(Integer.valueOf(map.get("department")[0]));
            student.setGrade(Byte.valueOf(map.get("grade")[0]));
            student.setMajorId(Integer.valueOf(map.get("major")[0]));
            student.setPhone(Long.valueOf(map.get("phone")[0]));
            logger.info("注册的学生信息：{}", student);
//            return new Result(Dictionary.SUCCESS);
            //验证信息是否有缺失
            if (student.getId() != null && student.getName() != null
                    && student.getPassword() != null && student.getSex() != null
                    && student.getDepartmentId() != null && student.getGrade() != null
                    && student.getMajorId() != null && student.getPhone() != null) {
                //执行注册操作
                return studentLoginService.studentRegister(student);
            } else {
                logger.info("缺少必要信息");
                return new Result(Dictionary.DATA_LOST);
            }
        } catch (NumberFormatException e1) {
            logger.error("非法数据：{}", e1.getMessage());
            return new Result(Dictionary.ILLEGAL);
        } catch (DataInsertException e2) {
            logger.error("数据插入失败：{}", e2.getMessage());
            return new Result(Dictionary.DATA_INSERT_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("系统异常：{}", e.getMessage());
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 教师注册
     *
     * @return 返回结果
     */
    @RequestMapping(value = "/teacherRegister", method = RequestMethod.POST)
    @ResponseBody
    public Result teacherRegister(HttpServletRequest request) {
        try {
            //获取教师实体信息
            Teacher teacher = new Teacher();
            Map<String, String[]> map = request.getParameterMap();
            teacher.setId(Long.parseLong(map.get("id")[0]));
            teacher.setName(map.get("name")[0]);
            teacher.setPassword(map.get("password")[0]);
            teacher.setSex(Byte.valueOf(map.get("sex")[0]));
            teacher.setEmail(map.get("email")[0]);
            teacher.setDepartmentId(Integer.valueOf(map.get("department")[0]));
            teacher.setPhone(Long.valueOf(map.get("phone")[0]));
            logger.info("注册的教师信息：{}", teacher);
            if (teacher.getId() != null && teacher.getName() != null
                    && teacher.getPassword() != null && teacher.getSex() != null
                    && teacher.getDepartmentId() != null && teacher.getPhone() != null) {
                //执行注册操作
                return teacherLoginService.teacherRegister(teacher);
            } else {
                logger.info("缺少必要信息");
                return new Result(Dictionary.DATA_LOST);
            }
        } catch (NumberFormatException e1) {
            return new Result(Dictionary.ILLEGAL);
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

    @RequestMapping(value = "/majorList")
    @ResponseBody
    public Result majorList(@RequestParam(value = "departmentId", required = false)Integer departmentId) {
        if (departmentId == null)
            return new Result(Dictionary.ILLEGAL);
        return new Result<>(true, studentHandle.selectByDepartmentId(departmentId));
    }
}
