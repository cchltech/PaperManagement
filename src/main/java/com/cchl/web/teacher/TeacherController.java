package com.cchl.web.teacher;

import com.cchl.dto.DataWithPage;
import com.cchl.dto.Result;
import com.cchl.entity.Title;
import com.cchl.entity.vo.TeacherMessage;
import com.cchl.eumn.Dictionary;
import com.cchl.service.teacher.TeacherHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/teacher")
public class TeacherController {

    @Autowired
    private TeacherHandle teacherHandle;

    Long testId = 10011L;
    Integer testUser = 1002;

    @GetMapping(value = "/hasNewMsg")
    public Result hasNewMsg(@SessionAttribute(value = "user_id", required = false) Integer userId) {
        try {
            userId = testUser;
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
        userId = testUser;
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
        id = testId;
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
            id = testId;
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
        id = testId;
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
        id = testId;
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

}
