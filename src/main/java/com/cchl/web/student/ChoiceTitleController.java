package com.cchl.web.student;

import com.cchl.dto.Result;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.NumberFullException;
import com.cchl.service.StudentHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 学生选题操作
 */
@Controller
@RequestMapping("/choice")
public class ChoiceTitleController {

    @Autowired
    private StudentHandle studentHandle;


    /**
     * 跳转到选题页面，查找出所有已经通过审核的题目，题目以列表方式展示
     * 学生只能选择本学院教师出的题目
     * 需要加上时间判断是否到了管理员指定的选题时间
     * 如果返回成功则加上授权提供选题操作
     * @param studentId 从session中获取学号
     * @return 题目实体集
     */
    @RequestMapping("/title")
    public Result getTitles(@SessionAttribute("student_id") Long studentId, HttpServletRequest request){
        /*
         * 先判断是否到了学生选题的时间,如果到了返回题目列表，否则返回剩余时间
         */
        Result result =  studentHandle.getTitleList(studentId);
        if (result.isSuccess()) {
            //TODO 添加权限
            HttpSession session = request.getSession();
            session.setAttribute("token", "key");
        }
        return result;
    }

    @RequestMapping(value = "/select")
    public Result select(@SessionAttribute(value = "user_id")int userId, @RequestParam(value = "titleId")int titleId) {
        try {
            return studentHandle.selectTitles(userId, titleId);
        } catch (NumberFullException e) {
            return new Result(Dictionary.NUMBER_IS_FULL);
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }

    }

}
