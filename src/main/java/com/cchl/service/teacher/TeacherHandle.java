package com.cchl.service.teacher;

import com.cchl.dao.*;
import com.cchl.entity.*;
import com.cchl.entity.vo.AdminMsg;
import com.cchl.entity.vo.TeacherMessage;
import com.cchl.entity.vo.UserMsgRecord;
import com.cchl.service.MsgHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherHandle {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String FilePath = "/home/beiyi/file/";

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TitleMapper titleMapper;
    @Autowired
    private PaperPlanMapper paperPlanMapper;
    @Autowired
    private MsgHandle msgHandle;

    @Autowired
    private WeeksPlanMapper weeksPlanMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private OpenReportMapper openReportMapper;
    @Autowired
    private MidCheckMapper midCheckMapper;

    public int insertTitle(Title title) {
        int departmentId = teacherMapper.selectById(title.getTeacherId()).getDepartment().getId();
        title.setDepartmentId(departmentId);
        msgHandle.addAdminMsg(AdminMsg.TYPE.TITLE, departmentId);
        return titleMapper.insert(title);
    }

    public List<Title> selectTitleList(Long id) {
        return titleMapper.selectByTeacherId(id);
    }

    public int deleteTitle(Long id, int titleId) {
        return titleMapper.deleteTitle(id, titleId);
    }

    public Teacher getInfoById(Long id) {
        return teacherMapper.selectById(id);
    }

    public int hasNewMsg(int userId) {
        /*
         * 先查找是否有当前用户的记录，
         * 有就找到当前用户消息记录的信息
         * 没有就新增一个
         */
        UserMsgRecord record = mongoTemplate.findById(userId, UserMsgRecord.class);
        if (record == null) {
            record = new UserMsgRecord();
            record.setId(userId);
            record.setType(1);
            record.setDepartmentId(teacherMapper.selectDepartmentIdByUserId(userId));
            record.setVersion(0);
            mongoTemplate.insert(record);
        }
        //查找消息列表中大于用户记录的版本号的
        Criteria criteria = Criteria.where("version").gt(record.getVersion());
        List<TeacherMessage> list = mongoTemplate.find(query(criteria), TeacherMessage.class);
        if (list != null && list.size() > 0)
            return list.size();
        else
            return 0;
    }

    public List<TeacherMessage> getMsg(int userId, int page) {
        UserMsgRecord record = mongoTemplate.findById(userId, UserMsgRecord.class);
        Criteria criteria = Criteria.where("departmentId").is(record.getDepartmentId());
        //查找消息
        List<TeacherMessage> list = mongoTemplate.find(
                query(criteria)
                        .with(new Sort(Sort.Direction.DESC, "version"))
                        .limit(5)
                        .skip((page-1)*5),
                TeacherMessage.class);
        if (page == 1 && list != null && list.size() > 0) {
            //更新用户的版本号
            mongoTemplate.updateFirst(query(Criteria.where("id").is(userId)),
                    new Update().set("version", list.get(0).getVersion()),
                    UserMsgRecord.class);
        }
        return list;
    }

    public int getMsgCount(int userId) {
        UserMsgRecord record = mongoTemplate.findById(userId, UserMsgRecord.class);
        Criteria criteria = Criteria.where("departmentId").is(record.getDepartmentId());
        return (int)mongoTemplate.count(query(criteria), TeacherMessage.class);
    }

    @Cacheable(value = "userId")
    public Integer getDepartmentId(int userId) {
        return teacherMapper.selectDepartmentIdByUserId(userId);
    }


    public int updatePhone(Long phone, Long id) {
        return teacherMapper.updatePhone(phone, id);
    }

    public int updateEmail(String email, Long id) {
        return teacherMapper.updateEmail(email, id);
    }

    public List<Student> selectStudentList(int titleId) {
        return studentMapper.selectByTitleId(titleId);
    }

    /**
     * 保存文件
     */
    public boolean saveFile(Integer useId, Integer titleId, String type, String fileName, byte[] file) {
        Integer paperId = paperPlanMapper.selectByTitleId(titleId);
        if (paperId == null)
            return false;
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
            return saveFilePath(type, paperId, useId, fileName);
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
                msgHandle.addAdminMsg(AdminMsg.TYPE.FILE, teacherMapper.selectDepartmentIdByUserId(id));
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

}
