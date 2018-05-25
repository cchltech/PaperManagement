package com.cchl.web;

import com.cchl.dto.Result;
import com.cchl.entity.Student;
import com.cchl.entity.Teacher;
import com.cchl.eumn.Dictionary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class TestController {

    @RequestMapping(value = "/test_registry")
    public String registry(){
        return "registry";
    }

    @PostMapping(value = "/test_student")
    @ResponseBody
    public Result student(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        for (Map.Entry<String,String[]> entry:map.entrySet()){
            System.out.print(entry.getKey() + " ");
        }
        return new Result(Dictionary.SUCCESS);
    }

    @RequestMapping(value = "/test_teacher")
    public Result teacher(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        for (Map.Entry<String,String[]> entry:map.entrySet()){
            System.out.print(entry.getKey() + " ");
        }
        return new Result(Dictionary.SUCCESS);
    }

}
