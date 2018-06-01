package com.cchl.service.student;

import com.cchl.dao.*;
import com.cchl.dto.Result;
import com.cchl.entity.*;
import com.cchl.entity.vo.StudentMessage;
import com.cchl.entity.vo.UserMsgRecord;
import com.cchl.eumn.Dictionary;
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

import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    @Autowired
    private MajorMapper majorMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

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

    public boolean saveFile(String filePath, String fileName,  byte[] file) {
        File target = new File(filePath);
        if (!target.exists()) {
            target.mkdirs();
        }
        try {
            FileOutputStream stream = new FileOutputStream(filePath + fileName);
            stream.write(file);
            stream.flush();
            stream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
