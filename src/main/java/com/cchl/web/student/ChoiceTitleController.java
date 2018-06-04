package com.cchl.web.student;

import com.cchl.dto.Result;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.NumberFullException;
import com.cchl.service.student.StudentHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 学生选题操作
 */
@RestController
@RequestMapping("/choice")
public class ChoiceTitleController {

    @Autowired
    private StudentHandle studentHandle;



}
