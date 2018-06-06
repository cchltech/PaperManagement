package com.cchl.service.admin;

import com.cchl.dao.*;
import com.cchl.dto.DataWithPage;
import com.cchl.dto.Result;
import com.cchl.entity.*;
import com.cchl.entity.vo.*;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.DataInsertException;
import com.cchl.execption.SystemException;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * 管理员的事项处理
 */
@Service
public class AdminHandle {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String FilePath = "/home/beiyi/file/";

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TitleMapper titleMapper;
    @Autowired
    private PaperPlanMapper paperPlanMapper;
    @Autowired
    private UserPaperMapper userPaperMapper;
    @Autowired
    private OpenReportMapper openReportMapper;
    @Autowired
    private MidCheckMapper midCheckMapper;
    //mongodb的操作类
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查找未选题的学生和选题未满的题目
     *
     * @param data 代表查找学生还是题目
     * @return
     */
    public DataWithPage allocate(String data, int userId) {
        int departmentId = getDepartmentIdByUserId(userId);
        if (data != null && "student".equals(data)) {
            return new DataWithPage<>(0, 0, studentMapper.selectUnTitle(departmentId));
        } else if (data != null && "title".equals(data)) {
            return new DataWithPage<>(0, 0, titleMapper.selectUnFull(departmentId));
        } else {
            return new DataWithPage(Dictionary.ILLEGAL_VISIT);
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
        Criteria criteria = Criteria.where("departmentId").is(departmentId).and("type").is(0);
        VersionRecord record = mongoTemplate.findOne(query(criteria), VersionRecord.class);
        int lastVersion = 0;
        if (record != null) {
            lastVersion = record.getVersion();
        } else {
            mongoTemplate.insert(new VersionRecord(0, departmentId, 0));
        }
        logger.info("查找到的版本号为：{}", lastVersion);
        //插入消息
        StudentMessage studentMessage = new StudentMessage();
        studentMessage.setContent(content);
        studentMessage.setDepartmentId(departmentId);
        studentMessage.setCreateTime(date);
        studentMessage.setVersion(++lastVersion);
        mongoTemplate.insert(studentMessage);
        mongoTemplate.updateFirst(query(criteria), Update.update("version", lastVersion), VersionRecord.class);
    }

    private void sendMsgToTeacher(String content, Integer departmentId, String date) {
        Criteria criteria = Criteria.where("departmentId").is(departmentId).and("type").is(1);
        //面向教师的系统信息插入
        VersionRecord record = mongoTemplate.findOne(query(criteria), VersionRecord.class);
        int lastVersion = 0;
        if (record != null) {
            lastVersion = record.getVersion();
        } else {
            mongoTemplate.insert(new VersionRecord(0, departmentId, 1));
        }
        logger.info("查找到的版本号为：{}", lastVersion);
        TeacherMessage teacherMessage = new TeacherMessage();
        teacherMessage.setContent(content);
        teacherMessage.setVersion(++lastVersion);
        teacherMessage.setCreateTime(date);
        teacherMessage.setDepartmentId(departmentId);
        mongoTemplate.insert(teacherMessage);
        mongoTemplate.updateFirst(query(criteria), Update.update("version", ++lastVersion), VersionRecord.class);
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
        String content = timer.getContent() + "，开始时间为：" + timer.getBegin() + ", 结束时间为：" + timer.getEnd() + "。请同学们做好准备";
        addMsg(0 ,content, timer.getDepartment());
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

    public List<FileInfo> midFileInfoList(Integer userId, int page, int limit) {
        int departmentId = getDepartmentIdByUserId(userId);
        List<FileInfo> fileInfos = new ArrayList<>();
        //获取中期检查
        List<MidCheck> midChecks = midCheckMapper.selectByDepartmentId(departmentId, page, limit);
        //封装数据
        for (MidCheck midCheck : midChecks) {
            int paperPlanId = midCheck.getPaperPlanId();
            Teacher teacher = teacherMapper.selectByPaperPlanId(paperPlanId);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(midCheck.getId());
            fileInfo.setFileName(midCheck.getFilePath());
            fileInfo.setTime(midCheck.getCreateTime());
            fileInfo.setId(teacher.getId());
            fileInfo.setName(teacher.getName());
            fileInfos.add(fileInfo);
        }
        return fileInfos;
    }

    public List<FileInfo> openFileInfoList(Integer userId, int page, int limit) {
        int departmentId = getDepartmentIdByUserId(userId);
        List<FileInfo> fileInfos = new ArrayList<>();
        //先获取开题报告
        List<OpenReport> openReports = openReportMapper.selectByDepartmentId(departmentId, page, limit);
        //封装数据
        for (OpenReport openReport : openReports) {
            int paperPlanId = openReport.getPaperPlanId();
            Student student = studentMapper.selectByPaperPlanId(paperPlanId);
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(openReport.getId());
            fileInfo.setFileName(openReport.getFilePath());
            fileInfo.setTime(openReport.getCreateTime());
            fileInfo.setId(student.getId());
            fileInfo.setName(student.getName());
            fileInfo.setMajor(student.getMajor().getName());
            fileInfos.add(fileInfo);
        }
        return fileInfos;
    }

    public int midCount(int userId) {
        int departmentId = getDepartmentIdByUserId(userId);
        return midCheckMapper.selectByDepartmentIdCount(departmentId);
    }

    public int openCount(int userId) {
        int departmentId = getDepartmentIdByUserId(userId);
        return openReportMapper.selectByDepartmentIdCount(departmentId);
    }

    public File getFile(Long id, String fileName, String type) {
        int userId;
        if ("MidCheck".equals(type))
            userId = studentMapper.selectById(id).getUserId();
        else
            userId = teacherMapper.selectById(id).getUserId();

        int paperId = userPaperMapper.selectByUserId(userId);
        return new File(FilePath + paperId + '/' + fileName);
    }

    public int examineFile(Integer id, String type, int status) {
        if ("MidCheck".equals(type))
            return midCheckMapper.updateStatus(id, status);
        else if ("OpenReport".equals(type))
            return openReportMapper.updateStatus(id, status);
        else
            return 0;
    }

    @Cacheable(cacheNames = "admin_userId",key = "#userId")
    public int getDepartmentIdByUserId(int userId) {
        return teacherMapper.selectDepartmentIdByUserId(userId);
    }
}
