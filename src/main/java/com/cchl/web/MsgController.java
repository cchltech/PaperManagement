package com.cchl.web;

import com.cchl.dto.Result;
import com.cchl.entity.vo.AdminMsg;
import com.cchl.eumn.Dictionary;
import com.cchl.service.MsgHandle;
import com.cchl.service.student.StudentHandle;
import com.cchl.service.teacher.TeacherHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/msg")
public class MsgController {

    @Autowired
    private TeacherHandle teacherHandle;
    @Autowired
    private StudentHandle studentHandle;
    @Autowired
    private MsgHandle msgHandle;

    //TODO 测试id
    int test_id = 1000;

    @PostMapping(value = "/get/{type}")
    @ResponseBody
    public Result getMsg(@PathVariable(value = "type")String type,
                         @SessionAttribute(value = "user_id", required = false)Integer userId) {
        if ("student".equals(type)) {
            userId = 1006;
            return new Result<>(true, msgHandle.getAdminMsg(studentHandle.selectDepartmentIdByUserId(userId)));
        } else if ("teacher".equals(type)) {
            userId = 1012;
            return new Result<>(true, msgHandle.getAdminMsg(teacherHandle.getDepartmentId(userId)));
        } else if ("admin".equals(type)){
            userId = test_id;
            return new Result<>(true, msgHandle.getAdminMsg(teacherHandle.getDepartmentId(userId)));
        } else {
            return new Result(Dictionary.ILLEGAL_VISIT);
        }
    }

    @RequestMapping(value = "/reset/{type}")
    @ResponseBody
    public Result resetMsg(@PathVariable(value = "type")String type,
                           @SessionAttribute(value = "user_id", required = false)Integer userId) {
        userId = test_id;
        if (AdminMsg.TYPE.USER.getType().equals(type)) {
            msgHandle.resetAdminMsg(AdminMsg.TYPE.USER, teacherHandle.getDepartmentId(userId));
            return new Result<>(Dictionary.SUCCESS);
        } else if (AdminMsg.TYPE.FILE.getType().equals(type)) {
            msgHandle.resetAdminMsg(AdminMsg.TYPE.FILE, teacherHandle.getDepartmentId(userId));
            return new Result<>(Dictionary.SUCCESS);
        } else if (AdminMsg.TYPE.TITLE.getType().equals(type)){
            msgHandle.resetAdminMsg(AdminMsg.TYPE.TITLE, teacherHandle.getDepartmentId(userId));
            return new Result(Dictionary.SUCCESS);
        } else {
            return new Result(Dictionary.ILLEGAL_VISIT);
        }
    }


}
