package com.cchl.web;

import com.cchl.dto.Result;
import com.cchl.entity.Student;
import com.cchl.entity.Teacher;
import com.cchl.eumn.Dictionary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestController {

    @RequestMapping(value = "/test_registry")
    public String registry(){
        return "registry";
    }

    @RequestMapping(value = "/test_student")
    public Result student(@RequestParam(value = "data", required = false)Student student) {
        System.out.println(student);
        return new Result(Dictionary.SUCCESS);
    }

    @RequestMapping(value = "/test_teacher")
    public Result teacher(@RequestParam(value = "data", required = false)Teacher teacher) {
        System.out.println(teacher);
        return new Result(Dictionary.SUCCESS);
    }

}
