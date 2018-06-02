package com.cchl.web.student;

import com.cchl.dto.DataWithPage;
import com.cchl.dto.Result;
import com.cchl.entity.vo.StudentMessage;
import com.cchl.eumn.Dictionary;
import com.cchl.service.student.StudentHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/student")
public class StudentController {

    //TODO 测试id
    private Long testId = 100111L;
    //TODO 测试id
    private Integer testUser = 1001;

    @Autowired
    private StudentHandle studentHandle;

    @PostMapping(value = "/update/{type}")
    public Result update(@PathVariable(value = "type")String type,
                         @RequestParam(value = "text")String text,
                         @SessionAttribute(value = "id", required = false)Long id) {
        id = testId;
        if ("phone".equals(type)) {
            try {
                Long phone = Long.valueOf(text);
                if (studentHandle.updatePhone(phone, id) > 0)
                    return new Result(Dictionary.SUCCESS);
                else
                    return new Result(Dictionary.SUBMIT_FAIL);
            } catch (NumberFormatException e) {
                return new Result(Dictionary.ILLEGAL);
            }
        } else if ("email".equals(type)) {
            if (studentHandle.updateEmail(text, id) > 0)
                return new Result(Dictionary.SUCCESS);
            else
                return new Result(Dictionary.SUBMIT_FAIL);
        } else {
            return new Result(Dictionary.ILLEGAL_VISIT);
        }
    }

    @PostMapping(value = "/upload/{type}")
    public Result upload(@PathVariable(value = "type") String type,
                         @RequestParam(value = "file")MultipartFile file,
                         @SessionAttribute(value = "userId", required = false)Integer userId) {
        try {
            userId = testUser;
            String fileName = file.getOriginalFilename();
            byte[] files = file.getBytes();
            if (studentHandle.saveFile(userId, fileName, files, type)) {
                return new Result(Dictionary.SUCCESS);
            } else {
                return new Result(Dictionary.SUBMIT_FAIL);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(Dictionary.SUBMIT_FAIL);
        }
    }

    @GetMapping(value = "/fileRecord")
    public DataWithPage fileRecord(@SessionAttribute(value = "user_id", required = false)Integer userId) {
        userId = testUser;
        return new DataWithPage<>(0,10,studentHandle.selectFileRecord(userId));
    }

    @RequestMapping(value = "/hasNewMsg")
    public Result hasNewMsg(@SessionAttribute(value = "user_id", required = false) Integer userId) {
        try {
            if (studentHandle.hasNewMsg(userId))
                return new Result(Dictionary.SUCCESS);
            else
                return new Result(Dictionary.NO_MORE_DATA);
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    @PostMapping(value = "/getMsg")
    public Result getMsg(@SessionAttribute(value = "user_id", required = false) Integer userId,
                         @RequestParam(value = "page", required = false) Integer page) {
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
