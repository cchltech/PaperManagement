package com.cchl.web;

import com.cchl.entity.Title;
import com.cchl.service.Impl.TeacherLoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * 论文题目控制类
 */
@Controller
public class TitleController {

    @Autowired
    private TeacherLoginServiceImpl teacherLoginService;

    /**
     * 登录到题目审批表的页面
     * @param id 操作的账号id
     * @return 返回结果
     */
    @RequestMapping(value = "/title", method = RequestMethod.POST)
    public String title(@SessionAttribute(value = "id") String id) {
        //需要教师才可以进行题目的申请操作
        if (teacherLoginService.isTeacher(Long.parseLong(id))) {
            return "title";
        } else {
            return "login";
        }
    }

    /**
     * 填写题目审批表
     * @param id 操作的账号id
     * @param title 题目审批表实体类
     * @return 返回结果
     */
    @RequestMapping(value = "/requestTitle", method = RequestMethod.POST)
    public String requestTitle(@SessionAttribute(value = "id") String id, @RequestBody(required = false)Title title) {
        if (teacherLoginService.isTeacher(Long.parseLong(id))) {

            return "title";
        } else {
            return "login";
        }
    }

}
