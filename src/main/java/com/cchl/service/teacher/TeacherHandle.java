package com.cchl.service.teacher;

import com.cchl.dao.TeacherMapper;
import com.cchl.dao.TitleMapper;
import com.cchl.entity.Teacher;
import com.cchl.entity.Title;
import com.cchl.entity.vo.TeacherMessage;
import com.cchl.entity.vo.UserMsgRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherHandle {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TitleMapper titleMapper;

    public int insertTitle(Title title) {
        title.setDepartmentId(teacherMapper.selectById(title.getTeacherId()).getDepartment().getId());
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

}
