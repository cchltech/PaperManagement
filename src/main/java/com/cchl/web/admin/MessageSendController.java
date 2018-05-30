package com.cchl.web.admin;

import com.cchl.dto.DataWithPage;
import com.cchl.dto.Result;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.IllegalVisitException;
import com.cchl.service.admin.AdminHandle;
import com.cchl.service.teacher.TeacherHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 消息发送控制类
 */
@RestController
@RequestMapping(value = "/msg")
public class MessageSendController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminHandle adminHandle;
    @Autowired
    private TeacherHandle teacherHandle;

    /**
     * 添加系统消息
     * @param content 消息内容
     * @return
     */
    @RequestMapping(value = "/add")
    public Result send(@RequestParam(value = "target") Integer target,
                       @RequestParam(value = "content", required = false)String content,
                       @SessionAttribute(value = "user_id", required = false)Integer userId) {
        try {
            userId = 1007;
            int departmentId = teacherHandle.getDepartmentId(userId);
            return adminHandle.addMsg(target, content, departmentId);
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/select/{type}")
    public DataWithPage select(@PathVariable(value = "type")String type,
                               @RequestParam(value = "page") int page,
                               @RequestParam(value = "limit") int limit) {
        try {
            if (type == null)
                throw new IllegalVisitException(Dictionary.ILLEGAL_VISIT);
            else if ("student".equals(type))
                return new DataWithPage<>(0,1, adminHandle.selectStudentMsg((page-1)*limit, limit));
            else if ("teacher".equals(type))
                return new DataWithPage<>(0,1, adminHandle.selectTeacherMsg((page-1)*limit, limit));
            else
                throw new IllegalVisitException(Dictionary.ILLEGAL_VISIT);
        } catch (IllegalVisitException e1) {
            logger.error("非法访问");
            return new DataWithPage(Dictionary.ILLEGAL_VISIT);
        } catch (Exception e2) {
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
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
