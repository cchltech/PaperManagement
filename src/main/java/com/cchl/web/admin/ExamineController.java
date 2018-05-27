package com.cchl.web.admin;

import com.cchl.dto.DataWithPage;
import com.cchl.dto.Result;
import com.cchl.entity.Title;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.SystemException;
import com.cchl.service.admin.ExamineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 返回到审核账号的数据
     *
     * @return 结果集
     */
    @RequestMapping("/user")
    public DataWithPage User(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        try {
            return new DataWithPage<>(0, examineService.totalNumber(0), examineService.users((page - 1) * limit, limit));
        } catch (Exception e) {
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/examineUser", method = RequestMethod.POST)
    public Result examineUser(@RequestParam(value = "ids", required = false) Integer[] ids, @RequestParam("status") Byte[] status) {
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
    @RequestMapping(value = "/title")
    public DataWithPage title(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        System.out.println(page+" "+limit);
        try {
            List<Title> list = examineService.title((page-1)*limit, limit);
            return new DataWithPage<>(0, examineService.totalNumber(1), list);
        } catch (Exception e) {
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/examineTitle")
    public Result examineTitle(@RequestParam(value = "ids", required = false) Integer[] ids, @RequestParam("status") Byte[] status) {
        try {
            if (ids != null && ids.length > 0 && status != null && status.length > 0) {
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

}
