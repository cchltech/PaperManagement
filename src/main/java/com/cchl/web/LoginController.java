package com.cchl.web;

import com.cchl.dto.Result;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.UnknowIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 注册与登录控制
 */
@Controller
public class LoginController {

    //日志记录
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     *
     * @return 跳转到初始页面
     */
    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    /**
     *
     * @return 跳转到注册页面
     */
    @RequestMapping(value = "/register")
    public String register(){
        return "register";
    }

    @RequestMapping(value = "/toRegister", method = RequestMethod.POST)
    public Result toRegister(@RequestParam(value = "id") String id, @RequestParam(value = "password") String password) {
        if (id != null && !"".equals(id.trim()) && password != null && !"".equals(password.trim())) {
            //账号密码不为空
            logger.info("注册成功");
            return new Result(true, Dictionary.SUCCESS);
        } else {
            logger.info("缺少id或密码");
            return new Result(false, Dictionary.DATA_LOST);
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
    public String login(String id, String password, String type) {
        try {
            /*
             * 判断登录身份
             */
            if ("student".equals(type)) {
                return "student";
            } else if ("teacher".equals(type)) {
                return "teacher";
            } else if ("admin".equals(type)) {
                return "admin";
            } else {
                logger.error("未知身份");
                throw new UnknowIdentity("未知身份");
            }
        } catch (Exception e) {
            logger.error("系统异常");
            return "error";
        }
    }
}
