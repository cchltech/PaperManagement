package com.cchl.web.student;

import com.cchl.service.student.StudentHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

/**
 * 学生页面跳转
 */
@Controller
@RequestMapping(value = "/student")
public class StudentURLController {

    @Autowired
    private StudentHandle studentHandle;

    @RequestMapping
    public String index() {
        return "student/student";
    }

    /**
     * 封装个人信息
     * @param id 学号
     * @return
     */
    @GetMapping(value = "/myInfo")
    public ModelAndView myInfo(@SessionAttribute(value = "id", required = false)Long id) {
        id = 100131L;
        ModelAndView model = new ModelAndView();
        model.setViewName("student/myInfo");
        model.addObject("student", studentHandle.selectById(id));
        return model;
    }

    @GetMapping(value = "/myPaper")
    public String myPaper() {
        return "student/myPaper";
    }

    @GetMapping(value = "/myFile")
    public String myFile() {
        return "student/myFile";
    }

    @GetMapping(value = "/choice")
    public String choice() {
        return "student/choice";
    }

    @GetMapping(value = "/downloadFile")
    public String downloadFile(){
        return "student/downloadFile";
    }

    @GetMapping(value = "/uploadFile")
    public String uploadFile() {
        return "student/uploadFile";
    }

    @GetMapping(value = "/msg")
    public String msg() {
        return "student/msg";
    }
}
