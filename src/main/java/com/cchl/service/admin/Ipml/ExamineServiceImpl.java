package com.cchl.service.admin.Ipml;

import com.cchl.dao.*;
import com.cchl.entity.PaperPlan;
import com.cchl.entity.Title;
import com.cchl.entity.User;
import com.cchl.entity.UserPaper;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.IllegalVisitException;
import com.cchl.execption.SystemException;
import com.cchl.service.InfoService;
import com.cchl.service.admin.ExamineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamineServiceImpl implements ExamineService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TitleMapper titleMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private InfoService infoService;
    @Autowired
    private PaperPlanMapper paperPlanMapper;
    @Autowired
    private UserPaperMapper userPaperMapper;

    @Override
    public int totalNumber(int type, int userId) {
        int result = 0;
        Integer departmentId = infoService.selectDepartmentId(userId);
        if (departmentId == null)
            throw new IllegalVisitException(Dictionary.ILLEGAL_VISIT);
        switch (type) {
            case 0:
                result = userMapper.totalNumber(false, (byte) 0, departmentId);
                break;
            case 1:
                result = titleMapper.totalNumber(false, (byte) 0, departmentId);
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public List<User> users(int userId, int page, int limit) {
        try {
            int departmentId = teacherMapper.selectDepartmentIdByUserId(userId);
            List<User> users = userMapper.selectUnaudited(departmentId, page, limit);
            for (User user : users) {
                user.happyGive();
                logger.info(user.toString());
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("异常信息：{}", e.getMessage());
            throw new SystemException(Dictionary.SYSTEM_ERROR);
        }
    }

    @Override
    public Integer[] resultUser(Integer[] id, Byte[] status) {
        try {
            Integer[] result = new Integer[status.length];
            for (int i = 0; i < status.length; i++) {
                /*
                 * 生成论文计划，同时关联use_paper
                 */
                result[i] = insertPlan(id[i], status[i]);
            }
            return result;
        } catch (Exception e) {
            throw new SystemException(Dictionary.SYSTEM_ERROR);
        }
    }

    @Override
    public List<Title> title(int page, int limit, int userId) {
        try {
            //查找用户的所属学院id
            Integer departmentId = teacherMapper.selectDepartmentIdByUserId(userId);
            if (departmentId == null) {
                throw new IllegalVisitException(Dictionary.ILLEGAL_VISIT);
            }
            return titleMapper.selectByStatus(page, limit, 0, false, departmentId);
        } catch (Exception e) {
            throw new SystemException(Dictionary.SYSTEM_ERROR);
        }
    }

    @Override
    public Integer[] resultTitle(Integer[] id, Byte[] status) {
        try {
            Integer[] result = new Integer[status.length];
            for (int i = 0; i < status.length; i++) {
                result[i] = titleMapper.updateStatus(id[i], status[i]);
            }
            return result;
        } catch (Exception e) {
            throw new SystemException(Dictionary.SYSTEM_ERROR);
        }
    }

    private int insertPlan(int id, int status) {
        if (status == 1) {
            //生成论文计划
            PaperPlan paperPlan = new PaperPlan();
            //题目id为0，默认没有选择的题目
            paperPlan.setTitleId(0);
            paperPlanMapper.insert(paperPlan);
            //关联用户与论文计划
            UserPaper userPaper = new UserPaper();
            userPaper.setPaperPlanId(paperPlan.getId());
            userPaper.setUserId(id);
            userPaperMapper.insert(userPaper);
            //更新状态
            return userMapper.updateStatus(id, (byte) status);
        } else {
            return userMapper.updateStatus(id, (byte) status);
        }
    }
}
