package com.cchl.service.student;

import com.cchl.dao.*;
import com.cchl.dto.Result;
import com.cchl.entity.*;
import com.cchl.entity.vo.*;
import com.cchl.eumn.Dictionary;
import com.cchl.eumn.TimerType;
import com.cchl.execption.IllegalVisitException;
import com.cchl.execption.NumberFullException;
import com.cchl.service.MsgHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * 处理学生端的所有操作
 */
@Service
public class StudentHandle {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String FilePath = "/home/beiyi/file/";

    private static final String salt = "GoodGoodStudyDayDayUp";

    @Autowired
    private ChoiceTitleMapper choiceTitleMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TitleMapper titleMapper;
    @Autowired
    private PaperPlanMapper paperPlanMapper;
    @Autowired
    private MajorMapper majorMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WeeksPlanMapper weeksPlanMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private OpenReportMapper openReportMapper;
    @Autowired
    private MidCheckMapper midCheckMapper;
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private UserPaperMapper userPaperMapper;

    @Autowired
    private MsgHandle msgHandle;

    @CacheEvict(cacheNames = "student", key = "#id")
    public int updateEmail(String value, Long id) {
        return studentMapper.updateEmail(value, id);
    }

    @CacheEvict(cacheNames = "student", key = "#id")
    public int updatePhone(Long value, Long id) {
        return studentMapper.updatePhone(value, id);
    }

    /**
     * 根据学号查找学生
     * @param id 学号
     * @return
     */
    @Cacheable(cacheNames = "student", key = "#id")
    public Student selectById(Long id) {
        return studentMapper.selectById(id);
    }

    public Result getTime(Long studentId) throws ParseException {
        /*
         * 先获取学生的学院id
         * 再判断是否到了选课时间
         */
        int departmentId = getDepartmentId(studentId);
        Criteria criteria = Criteria.where("department").is(departmentId).and("type").is(TimerType.CHOICE_TITLE.getType());
        List<VoTimer> timers = mongoTemplate.find(query(criteria), VoTimer.class);
        VoTimer timer = null;
        if (timers !=null && timers.size() > 0) {
            timer = timers.get(0);
        }
        if (timer != null) {
            long times = new Date().getTime();
            long begin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timer.getBegin()).getTime();
            //如果当前时间小于开始选题的时间
            if (times < begin) {
                logger.info("选题未开始，剩余时间：{}", (begin - times));
                //返回剩余时间
                return new Result<>(true, String.valueOf(new Date().getTime()), timer.getBegin());
            } else {
                long end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timer.getEnd()).getTime();
                //如果当前时间大于开始时间但小于结束时间
                if (times < end) {
                    return new Result<>(true, 0);
                } else {
                    //返回-1代表已经超时
                    return new Result<>(false, -1);
                }
            }
        } else {
            //返回-2代表未开始
            return new Result<>(false, -2);
        }
    }

    public List<Teacher> getTeacherList(Integer userId) {
        int departmentId = selectDepartmentIdByUserId(userId);
        return teacherMapper.selectHasTitleByDepartmentId(departmentId);
    }

    /**
     * @param studentId 学生学号
     * @return 获取题目列表
     */
    public List<Title> getTitleList(Long studentId, String content, Long teacherId, int page, int limit) throws ParseException {
        return getList(getDepartmentId(studentId), content, teacherId, page, limit);
    }

    /**
     * 选题操作
     *
     * @param titleId 选题的id
     * @param userId  用户id
     * @return 选题结果
     */
    @Transactional
    public Result selectTitles(int userId, String token, int titleId) {
        if (!token.equals(token(userId)))
            throw new IllegalVisitException(Dictionary.ILLEGAL_VISIT);
        //先在论文计划表中添加titleId,再将题目中的已选人数+1
        if (paperPlanMapper.insertTitle(userId, titleId) > 0) {
            if (titleMapper.updateTotal(titleId) > 0) {
                return new Result(Dictionary.SUCCESS);
            } else {
                //手动抛出异常，让事务回滚
                throw new NumberFullException(Dictionary.NUMBER_IS_FULL);
            }
        } else {
            return new Result<>(Dictionary.REPEAT_CHOICE);
        }
    }

    /**
     * 获取学生的学院号
     *
     * @param studentId 学号
     * @return 学院号
     */
    @Cacheable(cacheNames = "studentId")
    public int getDepartmentId(Long studentId) {
        return studentMapper.selectById(studentId).getDepartmentId();
    }

    /**
     * @return 获取题目列表
     */
    private List<Title> getList(int departmentId, String content, Long teacherId, int page, int limit) {
        return titleMapper.selectWithParam(departmentId, content, teacherId, page, limit);
    }

    public int totalOfTitle(Long studentId, String content, Long teacherId) {
        int departmentId = getDepartmentId(studentId);
        return titleMapper.totalOfTitle(departmentId, content, teacherId);
    }

    public int hasNewMsg(int userId) {
        UserMsgRecord record = mongoTemplate.findById(userId, UserMsgRecord.class);
        int departmentId = selectDepartmentIdByUserId(userId);
        if (record == null) {
            //新增一个用户记录
            record = new UserMsgRecord();
            record.setId(userId);
            record.setType(0);
            //查找用户所属的学院的id
            record.setDepartmentId(departmentId);
            record.setVersion(0);
            mongoTemplate.insert(record);
        }
        Criteria criteria = Criteria.where("version").gt(record.getVersion()).and("departmentId").is(departmentId);
        List<StudentMessage> list = mongoTemplate.find(query(criteria), StudentMessage.class);
        if (list == null || list.size() == 0)
            return 0;
        else
        return list.size();
    }

    public List<StudentMessage> getMsg(int userId, int page) {
        UserMsgRecord record = mongoTemplate.findById(userId, UserMsgRecord.class);
        Criteria criteria = Criteria.where("departmentId").is(record.getDepartmentId());
        List<StudentMessage> list = mongoTemplate.find(
                query(criteria)
                        .with(new Sort(Sort.Direction.DESC, "version"))
                        .limit(5)
                        .skip((page - 1) * 5),
                StudentMessage.class);
        if (page == 1 && list != null && list.size() > 0) {
            //查找页数为1时，肯定会查找到最新的消息，更新用户的版本号
            mongoTemplate.updateFirst(query(Criteria.where("id").is(userId)),
                    new Update().set("version", list.get(0).getVersion()),
                    UserMsgRecord.class);
        }
        return list;
    }

    public int getMsgCount(int userId) {
        UserMsgRecord record = mongoTemplate.findById(userId, UserMsgRecord.class);
        Criteria criteria = Criteria.where("departmentId").is(record.getDepartmentId());
        return (int)mongoTemplate.count(query(criteria), StudentMessage.class);
    }

    /**
     * 获取token
     * @return
     */
    public String getToken(Integer id) throws ParseException {
        int departmentId = selectDepartmentIdByUserId(id);
        Criteria criteria = Criteria.where("department").is(departmentId).and("type").is(TimerType.CHOICE_TITLE.getType());
        VoTimer timer = mongoTemplate.findOne(query(criteria), VoTimer.class);
        if (timer == null)
            return null;
        long begin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timer.getBegin()).getTime();
        long now = new Date().getTime();
        if (now >= begin) {
            //获取token
            return token(id);
        } else {
            return null;
        }
    }

    @Cacheable(cacheNames = "token", key = "#id")
    public String token(Integer id) {
        String token = DigestUtils.md5DigestAsHex((salt+id).getBytes());
        logger.info("生成的token值：{}", token);
        return token;
    }

    /**
     * 添加缓存项，免得过多的访问数据库
     * @param departmentId
     * @return
     */
    @Cacheable(cacheNames = "major",key = "#departmentId")
    public List<Major> selectByDepartmentId(Integer departmentId) {
        return majorMapper.selectByDepartmentId(departmentId);
    }

    @Cacheable(cacheNames = "departmentId", key = "#userId")
    public Integer selectDepartmentIdByUserId(int userId) {
        return studentMapper.selectByUserId(userId).getDepartmentId();
    }

    /**
     * 文件下载功能
     * @param userId
     * @return
     */
    public File getFile(Integer userId, String fileName) {
        Integer paperId = userPaperMapper.selectByUserId(userId).get(0);
        return new File(FilePath + paperId + '/' + fileName);
    }

    /**
     * 查找文件记录
     * @param userId
     * @return
     */
    public List<FileRecord> selectFileRecord(Integer userId) {
        //获取paper plan id
        Integer paperId = userPaperMapper.selectByUserId(userId).get(0);
        List<FileRecord> records = new ArrayList<>(5);
        //查找是否有周计划
        WeeksPlan weeksPlan = weeksPlanMapper.selectByPaperId(paperId);
        if (weeksPlan != null) {
            FileRecord record = new FileRecord("周计划", weeksPlan.getFilePath(), weeksPlan.getCreateTime());
            records.add(record);
        }
        //查找是否有任务书
        Task task = taskMapper.selectByPaperId(paperId);
        if (task != null) {
            FileRecord record = new FileRecord("任务书", task.getFilePath(), task.getCreateTime());
            records.add(record);
        }
        //查找是否有开题报告
        OpenReport openReport = openReportMapper.selectByPaperId(paperId);
        if (openReport != null) {
            FileRecord record = new FileRecord("开题报告", openReport.getFilePath(), openReport.getCreateTime());
            records.add(record);
        }
        //查找是否有中期检查
        MidCheck midCheck = midCheckMapper.selectByPaperId(paperId);
        if (midCheck != null) {
            FileRecord record = new FileRecord("中期检查", midCheck.getFilePath(), midCheck.getCreateTime());
            records.add(record);
        }
        //查找是否有论文
        Paper paper = paperMapper.selectByPaperId(paperId);
        if (paper != null) {
            FileRecord record = new FileRecord("论文", paper.getFilePath(), paper.getCreateTime());
            records.add(record);
        }
        return records;
    }

    /**
     * 保存文件
     * @param userId 账号
     * @param fileName 文件名
     * @param file 文件的二进制流
     * @param type 类型，比如中期检查等
     * @return
     */
    public boolean saveFile(Integer userId, String fileName,  byte[] file, String type) {
        List<Integer> paperIds = userPaperMapper.selectByUserId(userId);
        if (paperIds == null || paperIds.size() == 0)
            return false;
        Integer paperId = paperIds.get(0);
        String filePath = FilePath + paperId + '/';
        File target = new File(filePath);
        if (!target.exists()) {
            target.mkdirs();
        }
        try {
            FileOutputStream stream = new FileOutputStream(filePath + fileName);
            stream.write(file);
            stream.flush();
            stream.close();
            //保存文件路径
            return saveFilePath(type, paperId, userId, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveFilePath(String type, Integer paperId, Integer id, String filePath) {
        boolean success;
        switch (type) {
            case "WeeksPlan":
                success = saveWeekPlan(paperId, filePath) > 0;
                break;
            case "Task":
                success = saveTask(paperId, filePath) > 0;
                break;
            case "MidCheck":
                success = saveMidCheck(paperId, filePath) > 0;
                msgHandle.addAdminMsg(AdminMsg.TYPE.FILE, selectDepartmentIdByUserId(id));
                break;
            case "OpenReport":
                success = saveOpenReport(paperId, filePath) > 0;
                msgHandle.addAdminMsg(AdminMsg.TYPE.FILE, selectDepartmentIdByUserId(id));
                break;
            case "Paper":
                success = savePaper(paperId, filePath) > 0;
                break;
            default:
                success = false;
                break;
        }
        return success;
    }

    private int saveWeekPlan(Integer paperId, String filePath) {
        if (weeksPlanMapper.isExist(paperId) > 0) {
            return weeksPlanMapper.updateFilePath(paperId, filePath);
        } else {
            WeeksPlan weeksPlan = new WeeksPlan();
            weeksPlan.setFilePath(filePath);
            weeksPlan.setPaperPlanId(paperId);

            return weeksPlanMapper.insert(weeksPlan);
        }
    }
    private int saveTask(Integer paperId, String filePath) {
        if (taskMapper.isExist(paperId) > 0) {
            return taskMapper.updateFilePath(paperId, filePath);
        } else {
            Task task = new Task();
            task.setFilePath(filePath);
            task.setPaperPlanId(paperId);
            return taskMapper.insert(task);
        }
    }
    private int saveMidCheck(Integer paperId, String filePath) {
        if (midCheckMapper.isExist(paperId) > 0) {
            return midCheckMapper.updateFilePath(paperId,filePath);
        } else {
            MidCheck midCheck = new MidCheck();
            midCheck.setFilePath(filePath);
            midCheck.setPaperPlanId(paperId);
            return midCheckMapper.insert(midCheck);
        }
    }
    private int saveOpenReport(Integer paperId, String filePath) {
        if (openReportMapper.isExist(paperId) > 0) {
            return openReportMapper.updateFilePath(paperId, filePath);
        } else {
            OpenReport openReport = new OpenReport();
            openReport.setFilePath(filePath);
            openReport.setPaperPlanId(paperId);
            return openReportMapper.insert(openReport);
        }
    }
    private int savePaper(Integer paperId, String filePath) {
        if (paperMapper.isExist(paperId) > 0) {
            return paperMapper.updateFilePath(paperId, filePath);
        } else {
            Paper paper = new Paper();
            paper.setFilePath(filePath);
            paper.setScore(0);
            paper.setPaperPlanId(paperId);
            return paperMapper.insert(paper);
        }
    }

    public Title getMyTitle(Integer userId) {
        int paperPlanId = userPaperMapper.selectByUserId(userId).get(0);
        return titleMapper.selectByPaperPlanId(paperPlanId);
    }

    /**
     * 获取成绩
     */
    public Paper selectMyScore(Integer userId) {
        Integer paperId = userPaperMapper.selectByUserId(userId).get(0);
        Paper paper = paperMapper.selectByPaperId(paperId);
        if (paper == null) {
            paper = new Paper();
            paper.setScore(0);
            paper.setContent("未评价");
        } else {
            if (paper.getScore() == null)
                paper.setScore(0);
            if (paper.getContent() == null || "".equals(paper.getContent()))
                paper.setContent("未评价");
        }
        return paper;
    }
}
