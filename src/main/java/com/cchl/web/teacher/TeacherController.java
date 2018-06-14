package com.cchl.web.teacher;

import com.cchl.dto.DataWithPage;
import com.cchl.dto.Result;
import com.cchl.entity.Title;
import com.cchl.entity.vo.FileRecord;
import com.cchl.entity.vo.TeacherMessage;
import com.cchl.eumn.Dictionary;
import com.cchl.service.teacher.TeacherHandle;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/teacher")
public class TeacherController {

    @Autowired
    private TeacherHandle teacherHandle;

    @GetMapping(value = "/hasNewMsg")
    public Result hasNewMsg(@SessionAttribute(value = "user_id", required = false) Integer userId) {
        try {
            int num = teacherHandle.hasNewMsg(userId);
            if (num > 0)
                return new Result<>(true, num);
            else
                return new Result(Dictionary.NO_MORE_DATA);
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    @GetMapping(value = "/getMsg")
    public DataWithPage getMsg(@SessionAttribute(value = "user_id", required = false) Integer userId,
                               @RequestParam(value = "page", required = false) Integer page) {
        if (page == null)
            page = 1;
        try {
            List<TeacherMessage> list =  teacherHandle.getMsg(userId, page);
            return new DataWithPage<>(0, teacherHandle.getMsgCount(userId), list);
        } catch (Exception e) {
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }
    }

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
                if (teacherHandle.updatePhone(phone, id) > 0)
                    return new Result(Dictionary.SUCCESS);
                else
                    return new Result(Dictionary.SUBMIT_FAIL);
            } catch (NumberFormatException e) {
                return new Result(Dictionary.ILLEGAL);
            }
        } else if ("email".equals(type)) {
            if (teacherHandle.updateEmail(text, id) > 0)
                return new Result(Dictionary.SUCCESS);
            else
                return new Result(Dictionary.SUBMIT_FAIL);
        } else {
            return new Result(Dictionary.ILLEGAL_VISIT);
        }
    }

    /**
     * 填写题目审批表
     *
     * @param id    操作的账号id
     * @return 返回结果
     */
    @RequestMapping(value = "/requestTitle", method = RequestMethod.POST)
    public Result requestTitle(@SessionAttribute(value = "id", required = false) Long id,
                               @RequestParam(value = "content") String content,
                               @RequestParam(value = "instruction")String instruction,
                               @RequestParam(value = "totalNumber")String totalNumber) {
        try {
            if (content != null && instruction != null && totalNumber != null) {
                Title title = new Title();
                title.setContent(content);
                title.setInstruction(instruction);
                title.setTotalNumber(Integer.valueOf(totalNumber));
                title.setStatus((byte)0);
                title.setTeacherId(id);
                if (teacherHandle.insertTitle(title) > 0)
                    return new Result(Dictionary.SUCCESS);
                else
                    return new Result(Dictionary.SUBMIT_FAIL);
            } else
                return new Result(Dictionary.ILLEGAL);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 查找题目列表
     */
    @RequestMapping(value = "/title/list")
    public DataWithPage titleList(@SessionAttribute(value = "id", required = false)Long id) {
        try {
            return new DataWithPage<>(0, 0, teacherHandle.selectTitleList(id));
        } catch (Exception e) {
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 删除题目
     */
    @RequestMapping(value = "/title/delete")
    public Result deleteTitle(@RequestParam(value = "titleId")Integer titleId,
                              @SessionAttribute(value = "id", required = false)Long id) {
        try {
            if (teacherHandle.deleteTitle(id, titleId) > 0) {
                return new Result(Dictionary.SUCCESS);
            } else {
                return new Result(Dictionary.SUBMIT_FAIL);
            }
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 学生列表
     */
    @GetMapping(value = "/studentList")
    public DataWithPage studentList(@RequestParam(value = "titleId")Integer titleId) {
        if (titleId != null) {
            return new DataWithPage<>(0, 0, teacherHandle.  selectStudentList(titleId));
        } else {
            return new DataWithPage(Dictionary.ILLEGAL_VISIT);
        }
    }

    /**
     * 文件上传
     */
    @PostMapping(value = "/upload/{type}/{titleId}")
    public Result upload(@PathVariable(value = "type") String type,
                         @PathVariable(value = "titleId")Integer title,
                         @RequestParam(value = "file") MultipartFile file,
                         @SessionAttribute(value = "user_id", required = false) Integer userId) {
        try {
            if (teacherHandle.saveFile(userId, title, type, file.getOriginalFilename(), file.getBytes()))
                return new Result(Dictionary.SUCCESS);
            else return new Result(Dictionary.SUBMIT_FAIL);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(Dictionary.SUBMIT_FAIL);
        } catch (Exception e1) {
            e1.printStackTrace();
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 获取文件列表
     */
    @GetMapping(value = "/fileList")
    public DataWithPage fileList(@RequestParam(value = "titleId")Integer titleId) {
        try {
            return new DataWithPage<>(0, 0, teacherHandle.fileList(titleId));
        } catch (Exception e) {
            e.printStackTrace();
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 文件下载
     */
    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam(value = "paperPlanId")Integer paperPlanId,
                                               @RequestParam(value = "fileName")String fileName) {
        File file = teacherHandle.getFile(paperPlanId, fileName);
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            String downloadFileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            httpHeaders.setContentDispositionFormData("attachment", downloadFileName);
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            System.out.println("文件输出成功");
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
     * 获取题目申请的剩余时间
     */
    @GetMapping(value = "/time")
    public Result getTime(@SessionAttribute(value = "user_id", required = false) Integer userId) {
        try {
            /*
             * 先判断是否到了学生选题的时间,如果到了返回题目列表，否则返回剩余时间
             */
            return teacherHandle.getTime(userId);
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
     * 提交评价表
     */
    @PostMapping(value = "/rate")
    public Result rate(@RequestParam(value = "studentId")String studentId,
                       @RequestParam(value = "grade")String grade,
                       @RequestParam(value = "content")String content) {
        try {
            Long sId = Long.parseLong(studentId);
            Integer g = Integer.parseInt(grade);
            if (teacherHandle.updatePaper(sId, g, content) > 0)
                return new Result(Dictionary.SUCCESS);
            else
                return new Result(Dictionary.SUBMIT_FAIL);
        } catch (NumberFormatException e) {
            return new Result(Dictionary.ILLEGAL);
        }

    }

    @GetMapping(value = "/scoreList")
    public DataWithPage scoreList(@SessionAttribute(value = "id")Long id) {
        try{
            return new DataWithPage<>(0,0,teacherHandle.getScoreList(id));

        } catch (Exception e) {
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }

    }

}
