package com.cchl.web.student;

import com.cchl.dto.Result;
import com.cchl.entity.StudentMessage;
import com.cchl.eumn.Dictionary;
import com.cchl.service.student.StudentHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/student")
public class StudentController {

    @Autowired
    private StudentHandle studentHandle;

    @RequestMapping(value = "/hasNewMsg")
    public Result hasNewMsg(@SessionAttribute(value = "user_id") Integer userId) {
        try {
            if (studentHandle.hasNewMsg(userId))
                return new Result(Dictionary.SUCCESS);
            else
                return new Result(Dictionary.NO_MORE_DATA);
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/getMsg", method = RequestMethod.POST)
    public Result getMsg(@SessionAttribute(value = "user_id") Integer userId, @RequestParam(value = "page", required = false) Integer page) {
        if (page == null)
            page = 1;
        try {
            List<StudentMessage> list =  studentHandle.getMsg(userId, page);
            return new Result<>(true, list);
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }
}
