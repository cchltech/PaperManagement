package com.cchl.web.admin;

import com.cchl.dto.DataWithPage;
import com.cchl.dto.Result;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.DataInsertException;
import com.cchl.service.admin.AdminHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员为未选到体的学生进行题目调配
 */
@RestController
@RequestMapping(value = "/allocate")
public class AllocateController {

    int test_id = 1000;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminHandle adminHandle;


    @GetMapping(value = "/index/{data}")
    public DataWithPage getData(@PathVariable(value = "data") String data,
                                @SessionAttribute(value = "user_id", required = false) Integer userId) {
        try {
            userId = test_id;
            logger.info("调课操作，选择的数据为：{}", data);
            return adminHandle.allocate(data, userId);
        } catch (Exception e) {
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 进行调配操作，接收学生学号以及题目编号
     * 题目的已选人数+1，判断人数是否越界
     * 为学号对应的账户生成论文计划的记录，插入题目编号
     *
     * @return
     */
    @RequestMapping(value = "/operate", method = RequestMethod.POST)
    public Result operate(@RequestParam(value = "studentId", required = false) String studentId,
                          @RequestParam(value = "titleId", required = false) String titleId) {
        try {
            if (studentId == null || titleId == null)
                return new Result(Dictionary.DATA_LOST);
            return adminHandle.bindData(studentId, titleId);
        } catch (DataInsertException e1) {
            return new Result(Dictionary.DATA_INSERT_FAIL);
        } catch (Exception e) {
            logger.error("绑定学生与题目时系统发生异常，异常信息：{}", e.getMessage());
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }
}
