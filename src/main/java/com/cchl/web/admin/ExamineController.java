package com.cchl.web.admin;

import com.cchl.dto.Result;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.SystemException;
import com.cchl.service.ExamineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 审查控制类
 */
@RestController
@RequestMapping(value = "/examine")
public class ExamineController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 每页条数
     */
    private static final int NUMBER = 10;
    /**
     * 待审核用户总页数
     */
    private int userTotalPage;
    /**
     * 待审核题目总页数
     */
    private int titleTotalPage;

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
    @RequestMapping("/use")
    public Result User(@RequestParam(value = "page", required = false) Integer page) {
        try {
            //查找总页数，最小值为1
            if (userTotalPage == 0)
                userTotalPage = examineService.totalNumber(0) / NUMBER + 1;
            //初始化页数
            if (page == null)
                page = 1;
                //判断页数是否大于总页数
            else if (page > userTotalPage)
                return new Result(Dictionary.NO_MORE_DATA);
            return new Result<>(true, examineService.users((page - 1) * NUMBER, NUMBER));
        } catch (Exception e) {
            throw new SystemException(Dictionary.SYSTEM_ERROR);
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
    public Result title(@RequestParam(value = "page", required = false) Integer page) {
        try {
            //查找总页数，最小值为1
            if (titleTotalPage == 0)
                titleTotalPage = examineService.totalNumber(1) / NUMBER + 1;
            //初始化页数
            if (page == null)
                page = 1;
                //判断页数是否大于总页数
            else if (page > titleTotalPage)
                return new Result(Dictionary.NO_MORE_DATA);
            return new Result<>(true, examineService.title((page - 1) * NUMBER, NUMBER));
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
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
