package com.cchl.web.admin;

import com.cchl.dto.DataWithPage;
import com.cchl.dto.Result;
import com.cchl.entity.Title;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.IllegalVisitException;
import com.cchl.service.admin.ExamineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审查控制类
 */
@Controller
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
    @ResponseBody
    public DataWithPage User(@RequestParam(value = "page", required = false) Integer page,
                             @RequestParam(value = "limit", required = false) Integer limit,
                             @SessionAttribute(value = "user_id", required = false)Integer userId) {
        try {
            //TODO 测试用的userId
            userId = 1007;
            if (userId == null)
                return new DataWithPage(Dictionary.ILLEGAL_VISIT);
            return new DataWithPage<>(0, examineService.totalNumber(0, userId), examineService.users((page - 1) * limit, limit));
        } catch (IllegalVisitException e1) {
            return new DataWithPage(Dictionary.ILLEGAL_VISIT);
        } catch (Exception e) {
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/titleExamine")
    public String titleExamine() {
        return "admin/titleExamine";
    }

    @RequestMapping(value = "/examineUser", method = RequestMethod.POST)
    @ResponseBody
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
    @ResponseBody
    public DataWithPage title(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit, @SessionAttribute(value = "user_id", required = false)Integer userId) {
        try {
            //TODO 测试用的userId
            userId = 1007;
            if (userId == null) {
                return new DataWithPage(Dictionary.DATA_LOST);
            }
            List<Title> list = examineService.title((page-1)*limit, limit, userId);
            return new DataWithPage<>(0, examineService.totalNumber(1, userId), list);
        } catch (IllegalVisitException e1) {
            return new DataWithPage(Dictionary.ILLEGAL_VISIT);
        } catch (Exception e) {
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/examineTitle")
    @ResponseBody
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

    /**
     * 跳转到注册审核界面
     */
    @RequestMapping(value = "/examineUser")
    public String String() {return "admin/userExamine";}

}
