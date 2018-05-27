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

    @RequestMapping(value = "/admin")
    public String admin() {
        return "admin/admin";
    }

    @RequestMapping(value = "/titleExamine")
    public String titleExamine() {
        return "admin/titleExamine";
    }

    @RequestMapping(value = "/data/test")
    @ResponseBody
    public String test() {
        return "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":[{\"id\":1000,\"content\":\"java的未来\",\"instruction\":\"浅谈Java未来的发展方向\",\"status\":null,\"createTime\":1527289373000,\"totalNumber\":0,\"department\":null,\"has_select\":0}]}";
    }

}
