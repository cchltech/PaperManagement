package com.cchl.service;

import com.cchl.dao.ChoiceTitleMapper;
import com.cchl.dao.PaperPlanMapper;
import com.cchl.dao.StudentMapper;
import com.cchl.dao.TitleMapper;
import com.cchl.dto.Result;
import com.cchl.entity.ChoiceTitle;
import com.cchl.entity.Title;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.NumberFullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 处理学生端的所有操作
 */
@Service
public class StudentHandle {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ChoiceTitleMapper choiceTitleMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TitleMapper titleMapper;
    @Autowired
    private PaperPlanMapper paperPlanMapper;

    /**
     * @param studentId 学生学号
     * @return 获取题目列表
     */
    public Result getTitleList(Long studentId) {
        /*
         * 先获取学生的学院id
         * 再判断是否到了选课时间
         */
        int departmentId = getDepartmentId(studentId);
        ChoiceTitle choiceTitle = choiceTitleMapper.selectByDepartmentId(departmentId);
        long times = new Date().getTime();
        long begin = choiceTitle.getBeginTime().getTime();
        //如果当前时间小于开始选题的时间
        if (times < begin) {
            logger.info("选题为开始，剩余时间：{}", (begin - times));
            //返回剩余时间
            return new Result<>(false, (begin - times));
        } else {
            long end = choiceTitle.getEndTime().getTime();
            //如果当前时间大于开始时间但小于结束时间
            if (times < end) {
                return new Result<>(true, getList(departmentId));
            } else {
                //返回-1代表已经超时
                return new Result<>(false, -1);
            }
        }
    }

    /**
     * 选题操作
     *
     * @param titleId 选题的id
     * @param userId  用户id
     * @return 选题结果
     */
    @Transactional
    public Result selectTitles(int userId, int titleId) {
        //先在论文计划表中添加titleId,再将题目中的已选人数+1
        if (paperPlanMapper.insertTitle(userId, titleId) > 0) {
            if (titleMapper.updateTotal(titleId) > 0) {
                return new Result(Dictionary.SUCCESS);
            } else {
                //手动抛出异常，让事务回滚
                throw new NumberFullException(Dictionary.NUMBER_IS_FULL);
            }
        } else {
            return new Result<>(Dictionary.DATA_INSERT_FAIL);
        }
    }

    /**
     * 获取学生的学院号
     *
     * @param studentId 学号
     * @return 学院号
     */
    private int getDepartmentId(Long studentId) {
        return studentMapper.selectById(studentId).getDepartmentId();
    }

    /**
     * @param departmentId 学院号
     * @return 获取题目列表
     */
    private List<Title> getList(int departmentId) {
        return titleMapper.selectByDepartmentId(departmentId);
    }
}
