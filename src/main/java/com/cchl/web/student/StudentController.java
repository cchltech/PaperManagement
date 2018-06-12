package com.cchl.web.student;

import com.cchl.dto.DataWithPage;
import com.cchl.dto.Result;
import com.cchl.entity.Teacher;
import com.cchl.entity.vo.StudentMessage;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.IllegalVisitException;
import com.cchl.execption.NumberFullException;
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

    @Autowired
    private StudentHandle studentHandle;

    /**
     * 更新个人信息
     * @param type
     * @param text
     * @param id
     * @return
     */
    @PostMapping(value = "/update/{type}")
    public Result update(@PathVariable(value = "type") String type,
                         @RequestParam(value = "text") String text,
                         @SessionAttribute(value = "id", required = false) Long id) {
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

    /**
     * 上传文件
     * @param type
     * @param file
     * @param userId
     * @return
     */
    @PostMapping(value = "/upload/{type}")
    public Result upload(@PathVariable(value = "type") String type,
                         @RequestParam(value = "file") MultipartFile file,
                         @SessionAttribute(value = "user_id", required = false) Integer userId) {
        try {
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

    /**
     * 返回文件记录
     * @param userId
     * @return
     */
    @GetMapping(value = "/fileRecord")
    public DataWithPage fileRecord(@SessionAttribute(value = "user_id", required = false) Integer userId) {
        return new DataWithPage<>(0, 10, studentHandle.selectFileRecord(userId));
    }

    /**
     * 文件下载功能
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> download(@SessionAttribute(value = "user_id", required = false) Integer userId,
                                           @RequestParam(value = "fileName") String fileName) {
        File file = studentHandle.getFile(userId, fileName);
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            String downloadFileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            httpHeaders.setContentDispositionFormData("attachment", downloadFileName);
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(FileUtils.readFileToByteArray(file),
                    httpHeaders, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    /**
     * 判断是否有新消息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/hasNewMsg")
    public Result hasNewMsg(@SessionAttribute(value = "user_id", required = false) Integer userId) {
        try {
            int num = studentHandle.hasNewMsg(userId);
            if (num > 0)
                return new Result<>(true, num);
            else
                return new Result(Dictionary.NO_MORE_DATA);
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 获取新消息
     * @param userId
     * @param page
     * @return
     */
    @GetMapping(value = "/getMsg")
    public DataWithPage getMsg(@SessionAttribute(value = "user_id", required = false) Integer userId,
                         @RequestParam(value = "page", required = false) Integer page) {
        if (page == null)
            page = 1;
        try {
            List<StudentMessage> list = studentHandle.getMsg(userId, page);
            return new DataWithPage<>(0, studentHandle.getMsgCount(userId), list);
        } catch (Exception e) {
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 获取选题的剩余时间
     */
    @GetMapping(value = "/time")
    public Result getTime(@SessionAttribute(value = "id", required = false) Long studentId) {
        try {
            /*
             * 先判断是否到了学生选题的时间,如果到了返回题目列表，否则返回剩余时间
             */
            return studentHandle.getTime(studentId);
        } catch (ParseException e1) {
            e1.printStackTrace();
            System.out.println("时间格式转换异常");
            return new Result(Dictionary.SYSTEM_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 获取题目列表
     *
     * @param studentId 从session中获取学号
     * @return 题目实体集
     */
    @GetMapping("/title")
    public DataWithPage getTitles(@SessionAttribute(value = "id", required = false) Long studentId,
                                  @RequestParam(value = "content", required = false) String content,
                                  @RequestParam(value = "teacherId", required = false) String teacherId,
                                  @RequestParam(value = "page", required = false) Integer page,
                                  @RequestParam(value = "limit", required = false) Integer limit) {
        try {
            /*
             * 先判断是否到了学生选题的时间,如果到了返回题目列表，否则返回剩余时间
             */
            if (!studentHandle.getTime(studentId).isSuccess()) {
                return new DataWithPage(Dictionary.NOT_BEGINNING);
            }
            if (teacherId == null || "".equals(teacherId.trim()))
                teacherId = "0";
            long id = Long.parseLong(teacherId);
            int count = studentHandle.totalOfTitle(studentId, content, id);
            return new DataWithPage<>(0, count, studentHandle.getTitleList(studentId, content, id, (page - 1) * limit, limit));
        } catch (ParseException e1) {
            e1.printStackTrace();
            System.out.println("时间格式转换异常");
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 选题前先获取token
     *
     * @return
     */
    @GetMapping(value = "/getToken")
    public Result getToken(@SessionAttribute(value = "user_id", required = false) Integer userId) {
        try {
            String token = studentHandle.getToken(userId);
            if (token == null)
                return new Result<>(false, "选题未开始", -1);
            return new Result<>(true, token);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 选题操作
     *
     * @param userId  用户编号
     * @param titleId 题目id
     * @return
     */
    @PostMapping(value = "/select/{token}")
    public Result select(@SessionAttribute(value = "user_id", required = false) Integer userId,
                         @RequestParam(value = "titleId") int titleId,
                         @PathVariable(value = "token") String token) {
        try {
            return studentHandle.selectTitles(userId, token, titleId);
        } catch (NumberFullException e) {
            return new Result(Dictionary.NUMBER_IS_FULL);
        } catch (IllegalVisitException e1) {
            return new Result(Dictionary.ILLEGAL_VISIT);
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 获取教师列表
     */
    @GetMapping(value = "/teacherList")
    public List<Teacher> teachers(@SessionAttribute(value = "user_id", required = false) Integer userId) {
        return studentHandle.getTeacherList(userId);
    }

    /**
     * 模板下载
     */
    @GetMapping(value = "/template/{type}")
    public ResponseEntity<byte[]> getTemplate(@PathVariable(value = "type") String type) {
        String filePath = "/home/beiyi/file/template/";
        String fileName = type + ".docx";
        filePath += fileName;
        File file = new File(filePath);
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            String downloadFileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            httpHeaders.setContentDispositionFormData("attachment", downloadFileName);
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(FileUtils.readFileToByteArray(file),
                    httpHeaders, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping(value = "/titleInfo")
    public Result titleInfo(@SessionAttribute(value = "user_id", required = false)Integer userId) {
        try{
            return new Result<>(true, studentHandle.getMyTitle(userId));
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }

    }
}
