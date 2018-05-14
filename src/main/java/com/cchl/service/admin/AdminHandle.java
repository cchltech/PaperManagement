package com.cchl.service.admin;

import com.cchl.dao.*;
import com.cchl.dto.Result;
import com.cchl.entity.PaperPlan;
import com.cchl.entity.UserPaper;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.DataInsertException;
import com.cchl.execption.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理员的事项处理
 */
@Service
public class AdminHandle {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TitleMapper titleMapper;
    @Autowired
    private PaperPlanMapper paperPlanMapper;
    @Autowired
    private UserPaperMapper userPaperMapper;

    /**
     * 查找未选题的学生和选题未满的题目
     * @param data 代表查找学生还是题目
     * @return
     */
    public Result allocate(String data) {

        if (data != null && "student".equals(data)) {
            return new Result<>(true, studentMapper.selectUnTitle());
        } else if (data != null && "title".equals(data)) {
            return new Result<>(true, titleMapper.selectUnFull());
        } else {
            return new Result(Dictionary.ILLEGAL_VISIT);
        }
    }

    /**
     * 管理员为学生进行题目调配
     * @param studentId
     * @param titleId
     * @return
     */
    @Transactional
    public Result bindData(String studentId, String titleId) {
        try {
            //判断字符是否合法
            Integer tId = Integer.valueOf(titleId);
            Long sId = Long.valueOf(studentId);
            //题目人数+1
            if (titleMapper.updateTotal(tId) > 0) {
                //生成新的论文计划表，添加题目
                PaperPlan paperPlan = new PaperPlan();
                paperPlan.setTitleId(tId);
                if (paperPlanMapper.insert(paperPlan) > 0) {
                    //获取用户id
                    UserPaper userPaper = new UserPaper();
                    userPaper.setUserId(studentMapper.selectById(sId).getUserId());
                    userPaper.setPaperPlanId(paperPlan.getId());
                    //绑定用户与论文计划
                    if (userPaperMapper.insert(userPaper) > 0) {
                        return new Result(Dictionary.SUCCESS);
                    } else {
                        logger.error("绑定用户账户与论文计划失败");
                        throw new DataInsertException("绑定用户账户与论文计划失败");
                    }
                } else {
                    //插入数据失败
                    logger.error("插入论文计划表失败");
                    throw new DataInsertException("插入论文计划表失败");
                }
            } else {
                logger.error("该题目人数已满");
                return new Result(Dictionary.NUMBER_IS_FULL);
            }
        } catch (NumberFormatException e1) {
            return new Result(Dictionary.ILLEGAL);
        } catch (Exception e) {
            throw new SystemException(Dictionary.SYSTEM_ERROR);
        }
    }
}
