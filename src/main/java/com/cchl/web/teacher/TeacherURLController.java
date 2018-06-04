package com.cchl.web.teacher;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 负责教师的相关路径跳转工作
 */
@Controller
@RequestMapping(value = "/teacher")
public class TeacherURLController {

    /**
     * 跳转到教师主页面
     * @return
     */
    @RequestMapping
    public String teacher() {
        return "teacher/teacher";
    }

    /**
     * 跳转到任务书界面
     */
    @RequestMapping(value = "/planOfMission")
    public String mission() {return "teacher/planOfMission";}

    /**
     * 跳转到周计划页面
     */
    @RequestMapping(value = "/planOfWeek")
    public String planOfWeek() {
        return "teacher/planOfWeek";
    }

    /**
     * 跳转到中期检查页面
     */
    @RequestMapping(value = "/midCheck")
    public String midCheck() {
        return "teacher/midCheck";
    }

    /**
     * 跳转到题目审核页面
     */
    @RequestMapping(value = "/titleExamine")
    public String titleExamine() {
        return "teacher/titleExamine";
    }

    /**
     * 跳转到教师评价与成绩页面
     */
    @RequestMapping(value = "/valuationOfTeacher")
    public String valuationOfTeacher() {
        return "teacher/valuationOfTeacher";
    }

}
