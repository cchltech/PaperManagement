package com.cchl.web.teacher;

import com.cchl.dto.Result;
import com.cchl.entity.Title;
import com.cchl.eumn.Dictionary;
import com.cchl.service.admin.TitleHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 论文题目控制类
 */
@RestController
public class TitleController {

    @Autowired
    private TitleHandle titleHandle;

    /**
     * 登录到题目审批表的页面
     *
     * @param id 操作的账号id
     * @return 返回结果
     */
    @RequestMapping(value = "/title", method = RequestMethod.POST)
    public String title(@SessionAttribute(value = "id") String id) {
        //需要教师才可以进行题目的申请操作
        return "title";
    }

    /**
     * 填写题目审批表
     *
     * @param id    操作的账号id
     * @param title 题目审批表实体类
     * @return 返回结果
     */
    @RequestMapping(value = "/requestTitle", method = RequestMethod.POST)
    public Result requestTitle(@SessionAttribute(value = "id") Integer id, @RequestBody(required = false) Title title) {
        if (title == null)
            return new Result(Dictionary.ILLEGAL);
        else if (titleHandle.insert(title, id) > 0)
            return new Result(Dictionary.SUCCESS);
        else
            return new Result(Dictionary.SUBMIT_FAIL);
    }

}
