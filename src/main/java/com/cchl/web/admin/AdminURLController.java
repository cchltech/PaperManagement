package com.cchl.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 负责管理员的相关路径跳转工作
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminURLController {

    /**
     * 跳转到管理员主页面
     * @return
     */
    @RequestMapping
    public String admin() {
        return "admin/admin";
    }

    /**
     * 跳转到注册审核界面
     */
    @RequestMapping(value = "/examineUser")
    public String String() {return "admin/userExamine";}

    /**
     * 跳转到题目审核页面
     */
    @RequestMapping(value = "/titleExamine")
    public String titleExamine() {
        return "admin/titleExamine";
    }

    /**
     * 跳转到定时任务页面
     */
    @RequestMapping(value = "/timer")
    public String timer() {
        return "admin/timer";
    }
}
