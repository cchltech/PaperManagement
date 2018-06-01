package com.cchl.web.student;

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

    private static final String FilePath = "/home/beiyi/file/";

    @Autowired
    private StudentHandle studentHandle;

    @PostMapping(value = "/update/{type}")
    public Result update(@PathVariable(value = "type")String type,
                         @RequestParam(value = "text")String text,
                         @SessionAttribute(value = "id", required = false)Long id) {
        id = 100131L;
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

    @PostMapping(value = "/upload")
    public Result upload(@RequestParam(value = "file")MultipartFile file,
                         @SessionAttribute(value = "id", required = false)Long id) {
        try {
            id = 100131L;
            String filePath = FilePath + id + "/";
            String fileName = file.getOriginalFilename();
            byte[] files = file.getBytes();
            if (studentHandle.saveFile(filePath, fileName, files)) {
                return new Result(Dictionary.SUCCESS);
            } else {
                return new Result(Dictionary.SUBMIT_FAIL);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(Dictionary.SUBMIT_FAIL);
        }
    }

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

    @PostMapping(value = "/getMsg")
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
