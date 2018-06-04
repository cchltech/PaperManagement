package com.cchl.web.student;

import com.cchl.dto.DataWithPage;
import com.cchl.dto.Result;
import com.cchl.entity.vo.StudentMessage;
import com.cchl.eumn.Dictionary;
import com.cchl.service.student.StudentHandle;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
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
                         @SessionAttribute(value = "user_id", required = false)Integer userId) {
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

    /**
     * 文件下载功能
     * @param userId
     * @return
     */
    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> download(@SessionAttribute(value = "user_id", required = false)Integer userId,
                                           @RequestParam(value = "fileName") String fileName) {
        userId = testUser;
        File file = studentHandle.getFile(userId, fileName);
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            String downloadFileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            httpHeaders.setContentDispositionFormData("attachment", downloadFileName);
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("文件输出成功");
            return new ResponseEntity<>(FileUtils.readFileToByteArray(file),
                    httpHeaders, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
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

    /**
     * 跳转到选题页面，查找出所有已经通过审核的题目，题目以列表方式展示
     * 学生只能选择本学院教师出的题目
     * 需要加上时间判断是否到了管理员指定的选题时间
     * 如果返回成功则加上授权提供选题操作
     * @param studentId 从session中获取学号
     * @return 题目实体集
     */
    @GetMapping("/title")
    public Result getTitles(@SessionAttribute(value = "id", required = false) Long studentId, HttpServletRequest request){
        try {
            /*
             * 先判断是否到了学生选题的时间,如果到了返回题目列表，否则返回剩余时间
             */
            studentId = testId;
            Result result = studentHandle.getTitleList(studentId);
            if (result.isSuccess()) {
                //TODO 添加权限
                HttpSession session = request.getSession();
                session.setAttribute("token", "key");
            }
            return result;
        } catch (ParseException e1) {
            e1.printStackTrace();
            System.out.println("时间格式转换异常");
            return new Result(Dictionary.SYSTEM_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }
}
