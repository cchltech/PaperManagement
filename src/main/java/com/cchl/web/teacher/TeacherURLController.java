package com.cchl.web.teacher;

import com.cchl.service.teacher.TeacherHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

/**
 * 负责教师的相关路径跳转工作
 */
@Controller
@RequestMapping(value = "/teacher")
public class TeacherURLController {

    @Autowired
    private TeacherHandle teacherHandle;


    /**
     * 跳转到教师主页面
     * @return
     */
    @RequestMapping
    public String teacher() {
        return "teacher/teacher";
    }

    /**
     * 跳转到个人信息页面
     */
    @RequestMapping(value = "/myInfo")
    public ModelAndView myInfo(@SessionAttribute(value = "id", required = false)Long id) {
        ModelAndView modelAndView = new ModelAndView("teacher/myInfo");
        modelAndView.addObject("teacher", teacherHandle.getInfoById(id));
        return modelAndView;
    }

    /**
     * 跳转到文件处理
     */
    @RequestMapping(value = "/fileOperation")
    public String mission() {return "teacher/fileOperation";}

    /**
     * 跳转到文件下载页面
     */
    @RequestMapping(value = "/fileDownload")
    public String fileDownload() {
        return "teacher/fileDownload";
    }

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

    /**
     * 跳转到系统消息页面
     */
    @RequestMapping(value = "/msg")
    public String msg() {
        return "teacher/msg";
    }

}
