package com.cchl.web.admin;

import com.cchl.dto.DataWithPage;
import com.cchl.dto.Result;
import com.cchl.entity.Title;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.IllegalVisitException;
import com.cchl.service.admin.AdminHandle;
import com.cchl.service.admin.ExamineService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 审查控制类
 */
@RestController
@RequestMapping(value = "/examine")
public class ExamineController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 注入审核处理的bean
     */
    @Autowired
    private ExamineService examineService;
    @Autowired
    private AdminHandle adminHandle;

    /**
     * 返回到审核账号的数据
     *
     * @return 结果集
     */
    @GetMapping("/user")
    public DataWithPage User(@RequestParam(value = "page", required = false) Integer page,
                             @RequestParam(value = "limit", required = false) Integer limit,
                             @SessionAttribute(value = "user_id", required = false)Integer userId) {
        try {
            if (userId == null)
                return new DataWithPage(Dictionary.ILLEGAL_VISIT);
            return new DataWithPage<>(0, examineService.totalNumber(0, userId, 0), examineService.users(userId, (page - 1) * limit, limit));
        } catch (IllegalVisitException e1) {
            return new DataWithPage(Dictionary.ILLEGAL_VISIT);
        } catch (Exception e) {
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }
    }

    @PostMapping(value = "/examineUser")
    public Result examineUser(@RequestParam(value = "ids", required = false) Integer[] ids,
                              @RequestParam("status") Byte[] status) {
        try {
            if (ids != null && ids.length > 0 && status != null && status.length > 0) {
                Integer[] results = examineService.resultUser(ids, status);
                if (results.length == 0)
                    return new Result<>(false, "插入数据出现错误", results);
                return new Result<>(Dictionary.SUCCESS);
            } else {
                logger.error("提交审核数据时缺少参数");
                return new Result<>(Dictionary.DATA_LOST);
            }
        } catch (Exception e) {
            return new Result<>(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * @return 返回待审核的题目列
     */
    @GetMapping(value = "/title")
    public DataWithPage title(@RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "limit", required = false) Integer limit,
                              @SessionAttribute(value = "user_id", required = false)Integer userId,
                              @SessionAttribute(value = "type", required = false)Integer type) {
        try {
            if (userId == null) {
                return new DataWithPage(Dictionary.DATA_LOST);
            }
            List<Title> list = examineService.title((page-1)*limit, limit, userId, type-1);
            return new DataWithPage<>(0, examineService.totalNumber(1, userId, type-1), list);
        } catch (IllegalVisitException e1) {
            return new DataWithPage(Dictionary.ILLEGAL_VISIT);
        } catch (Exception e) {
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }
    }

    @PostMapping(value = "/examineTitle")
    public Result examineTitle(@RequestParam(value = "ids", required = false) Integer[] ids,
                               @SessionAttribute(value = "type", required = false)Integer type) {
        try {
            if (ids != null && ids.length > 0) {
                Byte[] status = new Byte[]{(byte)((int)type)};
                Integer[] results = examineService.resultTitle(ids, status);
                if (results.length == 0)
                    return new Result<>(false, "插入数据出现错误", results);
                return new Result<>(Dictionary.SUCCESS);
            } else {
                logger.error("提交审核数据时缺少参数");
                return new Result<>(Dictionary.DATA_LOST);
            }
        } catch (Exception e) {
            return new Result<>(Dictionary.SYSTEM_ERROR);
        }
    }

    @GetMapping(value = "/midFile")
    public DataWithPage midFile(@RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "limit", required = false) Integer limit,
                                @SessionAttribute(value = "user_id", required = false)Integer userId,
                                @SessionAttribute(value = "type", required = false)Integer type) {
        return new DataWithPage<>(0, adminHandle.midCount(userId, type), adminHandle.midFileInfoList(userId, type, (page-1)*limit, limit));
    }

    @GetMapping(value = "/openFile")
    public DataWithPage openFile(@RequestParam(value = "page", required = false) Integer page,
                                 @RequestParam(value = "limit", required = false) Integer limit,
                                 @SessionAttribute(value = "user_id", required = false)Integer userId,
                                 @SessionAttribute(value = "type", required = false)Integer type) {
        return new DataWithPage<>(0, adminHandle.openCount(userId, type), adminHandle.openFileInfoList(userId, type,(page-1)*limit, limit));
    }

    /**
     * 审核时下载页面
     * @param id 学号
     * @param fileName 文件名
     * @return
     */
    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> download(@RequestParam(value = "id") String id,
                                           @RequestParam(value = "fileName") String fileName,
                                           @RequestParam(value = "type")String type) {
        File file = adminHandle.getFile(Long.parseLong(id), fileName, type);
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

    /**
     * 审核文件
     */
    @PostMapping(value = "/examineFile/{type}")
    public Result examineFile(@RequestParam(value = "fileId")Integer id,
                              @PathVariable(value = "type")String type,
                              @SessionAttribute(value = "type")Integer status) {
        if (adminHandle.examineFile(id, type, status) > 0)
            return new Result(Dictionary.SUCCESS);
        else
            return new Result(Dictionary.SUBMIT_FAIL);
    }

}
