package com.cchl.web.admin;

import com.cchl.dto.Result;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.IllegalVisitException;
import com.cchl.service.admin.AdminHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 消息发送控制类
 */
@Controller
@RequestMapping(value = "/msg")
public class MessageSendController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminHandle adminHandle;

    /**
     * 添加系统消息
     * @param content 消息内容
     * @param departmentId 学院id， 如果面向的用户类型为教师则不需要学院id
     * @return
     */
    @RequestMapping(value = "/add")
    public Result send(@RequestParam(value = "content", required = false)String content,
                       @RequestParam(value = "departmentId",required = false) Integer departmentId) {
        try {
            return adminHandle.addMsg(content, departmentId);
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/select/{type}")
    public Result select(@PathVariable(value = "type")String type,
                         @RequestParam(value = "page") int page) {
        try {
            if (type == null)
                throw new IllegalVisitException(Dictionary.ILLEGAL_VISIT);
            else if ("student".equals(type))
                return new Result<>(true, adminHandle.selectStudentMsg(page));
            else if ("teacher".equals(type))
                return new Result<>(true, adminHandle.selectTeacherMsg(page));
            else
                throw new IllegalVisitException(Dictionary.ILLEGAL_VISIT);
        } catch (IllegalVisitException e1) {
            logger.error("非法访问");
            return new Result(Dictionary.ILLEGAL_VISIT);
        } catch (Exception e2) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    /**
     * 删除消息
     * @param departmentId
     * @param version
     * @return
     */
    @RequestMapping(value = "/deleteMsg")
    public Result deleteMsg(@RequestParam(value = "departmentId", required = false) Integer departmentId,
                            @RequestParam(value = "version", required = false) Integer version) {
        try {
            if (departmentId == null)
                adminHandle.deleteTeacherMsg(version);
            else
                adminHandle.deleteStudentMsg(departmentId, version);
            return new Result(Dictionary.SUCCESS);
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }
}
