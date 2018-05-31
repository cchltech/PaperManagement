package com.cchl.service.admin;

import com.cchl.dao.PaperPlanMapper;
import com.cchl.dao.StudentMapper;
import com.cchl.dao.TitleMapper;
import com.cchl.dao.UserPaperMapper;
import com.cchl.dto.Result;
import com.cchl.entity.PaperPlan;
import com.cchl.entity.vo.StudentMessage;
import com.cchl.entity.vo.TeacherMessage;
import com.cchl.entity.UserPaper;
import com.cchl.entity.vo.VoTimer;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.DataInsertException;
import com.cchl.execption.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

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
    //mongodb的操作类
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查找未选题的学生和选题未满的题目
     *
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
     *
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

    /**
     * 专业管理员发送系统消息
     * @param content 消息内容
     * @return
     */
    public Result addMsg(Integer target, String content, Integer departmentId) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        logger.info("插入数据的时间：{}", date);
        try {
            if (target == 0) {
                sendMsgToStudent(content, departmentId, date);
                return new Result(Dictionary.SUCCESS);
            } else if (target == 1){
                sendMsgToTeacher(content, departmentId, date);
                return new Result(Dictionary.SUCCESS);
            } else {
                sendMsgToStudent(content, departmentId, date);
                sendMsgToTeacher(content, departmentId, date);
                return new Result(Dictionary.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    private void sendMsgToStudent(String content, Integer departmentId, String date) {
        //先查找当前数据库中该学院的最新一个版本号
        Criteria criteria = Criteria.where("departmentId").is(departmentId);
        List<StudentMessage> messages = mongoTemplate.find(
                query(criteria)
                        .with(new Sort(Sort.Direction.DESC, "version"))
                        .limit(1)
                , StudentMessage.class);
        int lastVersion = 0;
        if (messages.size() > 0)
            lastVersion = messages.get(0).getVersion();
        logger.info("查找到的版本号为：{}", lastVersion);
        //插入消息
        StudentMessage studentMessage = new StudentMessage();
        studentMessage.setContent(content);
        studentMessage.setDepartmentId(departmentId);
        studentMessage.setCreateTime(date);
        studentMessage.setVersion(++lastVersion);
        mongoTemplate.insert(studentMessage);
    }

    private void sendMsgToTeacher(String content, Integer departmentId, String date) {
        Criteria criteria = Criteria.where("departmentId").is(departmentId);
        //面向教师的系统信息插入
        List<TeacherMessage> messages = mongoTemplate.find(
                query(criteria)
                        .with(new Sort(Sort.Direction.DESC, "version"))
                        .limit(1)
                , TeacherMessage.class);
        int lastVersion = 0;
        if (messages.size() > 0)
            lastVersion = messages.get(0).getVersion();
        logger.info("查找到的版本号为：{}", lastVersion);
        TeacherMessage teacherMessage = new TeacherMessage();
        teacherMessage.setContent(content);
        teacherMessage.setVersion(++lastVersion);
        teacherMessage.setCreateTime(date);
        teacherMessage.setDepartmentId(departmentId);
        mongoTemplate.insert(teacherMessage);
    }

    public List<StudentMessage> selectStudentMsg(int page, int limit) {
        //查找面向学生的消息
        return mongoTemplate.find(
                query(new Criteria())
                        .with(new Sort(Sort.Direction.DESC, "createTime"))
                        .limit(limit)
                        .skip(page)
                , StudentMessage.class);
    }

    public List<TeacherMessage> selectTeacherMsg(int page, int limit) {
        return mongoTemplate.find(
                query(new Criteria())
                    .with(new Sort(Sort.Direction.DESC, "createTime"))
                    .limit(limit)
                    .skip(page)
                , TeacherMessage.class);
    }

    public void deleteStudentMsg(int departmentId, int version) {
        Criteria criteria = Criteria.where("departmentId").is(departmentId).and("version").is(version);
        mongoTemplate.remove(query(criteria), StudentMessage.class);
    }

    public void deleteTeacherMsg(int departmentId, int version) {
        mongoTemplate.remove(query(Criteria.where("version").is(version).and("departmentId").is(departmentId)), TeacherMessage.class);
    }

    /**
     * 添加定时任务
     * @param timer
     */
    public void addTimer(VoTimer timer) {
        int num = (int)mongoTemplate.count(query(new Criteria()), VoTimer.class);
        timer.setId(num + 1000);
        mongoTemplate.insert(timer);
    }
    /**
     * 更新定时任务
     */
    public void updateTimer(int id, String content, String begin, String end) {
        Criteria criteria = Criteria.where("id").is(id);
        Update update = Update.update("content", content).set("begin", begin).set("end", end);
        mongoTemplate.updateFirst(query(criteria), update, VoTimer.class);
    }

    /**
     * 删除定时任务
     * @param id
     */
    public void removeTimer(Integer id) {
        Criteria criteria = Criteria.where("id").is(id);
        mongoTemplate.remove(query(criteria), VoTimer.class);
    }

    /**
     * 按学院号查找目标记录
     * @param department
     * @param page
     * @param limit
     * @return
     */
    public List<VoTimer> selectByDepartmentId(int department, int page, int limit) {
        Criteria criteria = Criteria.where("department").is(department);
        return mongoTemplate.find(query(criteria).skip(page).limit(limit), VoTimer.class);
    }

    public int TimerCount(int department) {
        Criteria criteria = Criteria.where("department").is(department);
        return (int)mongoTemplate.count(query(criteria), VoTimer.class);
    }
}
