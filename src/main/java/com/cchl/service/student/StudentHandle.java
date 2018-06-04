package com.cchl.service.student;

import com.cchl.dao.*;
import com.cchl.dto.Result;
import com.cchl.entity.*;
import com.cchl.entity.vo.FileRecord;
import com.cchl.entity.vo.StudentMessage;
import com.cchl.entity.vo.UserMsgRecord;
import com.cchl.entity.vo.VoTimer;
import com.cchl.eumn.Dictionary;
import com.cchl.eumn.TimerType;
import com.cchl.execption.NumberFullException;
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
                return new Result<>(true, timer.getBegin());
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

    public boolean hasNewMsg(int userId) {
        UserMsgRecord record = mongoTemplate.findById(userId, UserMsgRecord.class);
        if (record == null) {
            //新增一个用户记录
            record = new UserMsgRecord();
            record.setId(userId);
            record.setType(0);
            //查找用户所属的学院的id
            record.setDepartmentId(studentMapper.selectByUserId(userId).getDepartmentId());
            record.setVersion(0);
            mongoTemplate.insert(record);
        }
        Criteria criteria = Criteria.where("version").gt(record.getVersion());
        List<StudentMessage> list = mongoTemplate.find(query(criteria), StudentMessage.class);
        return list != null && list.size() > 0;
    }

    public List<StudentMessage> getMsg(int userId, int page) {
        UserMsgRecord record = mongoTemplate.findById(userId, UserMsgRecord.class);
        Criteria criteria = Criteria.where("departmentId").is(record.getDepartmentId());
        List<StudentMessage> list = mongoTemplate.find(
                query(criteria)
                        .with(new Sort(Sort.Direction.DESC, "version"))
                        .limit(10)
                        .skip((page - 1) * 10),
                StudentMessage.class);
        if (page == 1) {
            //查找页数为1时，肯定会查找到最新的消息，更新用户的版本号
            mongoTemplate.updateFirst(query(Criteria.where("id").is(userId)),
                    new Update().set("version", list.get(0).getVersion()),
                    UserMsgRecord.class);
        }
        return list;
    }

    /**
     * 获取token
     * @return
     */
    public String getToken(Integer id) throws ParseException {
        int departmentId = selectDepartmentIdByUserId(id);
        Criteria criteria = Criteria.where("department").is(departmentId).and("type").is(TimerType.CHOICE_TITLE.getType());
        VoTimer timer = mongoTemplate.find(query(criteria), VoTimer.class).get(0);
        long begin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timer.getBegin()).getTime();
        long now = new Date().getTime();
        if (now >= begin) {
            //获取token
            return token(id);
        } else {
            return null;
        }
    }

    @Cacheable(value = "token")
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
    @Cacheable("departmentId")
    public List<Major> selectByDepartmentId(Integer departmentId) {
        return majorMapper.selectByDepartmentId(departmentId);
    }

    @Cacheable(value = "userId")
    public Integer selectDepartmentIdByUserId(int userId) {
        return studentMapper.selectByUserId(userId).getDepartmentId();
    }

    /**
     * 文件下载功能
     * @param userId
     * @return
     */
    public File getFile(Integer userId, String fileName) {
        Integer paperId = userPaperMapper.selectByUserId(userId);
        return new File(FilePath + paperId + '/' + fileName);
    }

    /**
     * 查找文件记录
     * @param userId
     * @return
     */
    public List<FileRecord> selectFileRecord(Integer userId) {
        //获取paper plan id
        Integer paperId = userPaperMapper.selectByUserId(userId);
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
        Integer paperId = userPaperMapper.selectByUserId(userId);
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
            return saveFilePath(type, userId, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveFilePath(String type, Integer id, String filePath) {
        boolean success;
        Integer paperId = userPaperMapper.selectByUserId(id);
        System.out.println("论文计划ID为："+paperId + " 类型为：" + type);
        if (paperId == null)
            return false;
        switch (type) {
            case "WeeksPlan":
                success = saveWeekPlan(paperId, filePath) > 0;
                break;
            case "Task":
                success = saveTask(paperId, filePath) > 0;
                break;
            case "MidCheck":
                success = saveMidCheck(paperId, filePath) > 0;
                break;
            case "OpenReport":
                success = saveOpenReport(paperId, filePath) > 0;
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
}
