package com.cchl.service.teacher;

import com.cchl.dao.TeacherMapper;
import com.cchl.entity.vo.TeacherMessage;
import com.cchl.entity.vo.UserMsgRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public boolean hasNewMsg(int userId) {
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
            record.setVersion(0);
            mongoTemplate.insert(record);
        }
        //查找消息列表中大于用户记录的版本号的
        Criteria criteria = Criteria.where("version").gt(record.getVersion());
        List<TeacherMessage> list = mongoTemplate.find(query(criteria), TeacherMessage.class);
        return list != null && list.size() > 0;
    }

    public List<TeacherMessage> getMsg(int userId, int page) {
        //查找消息
        List<TeacherMessage> list = mongoTemplate.find(
                query(new Criteria())
                        .with(new Sort(Sort.Direction.DESC, "version"))
                        .limit(10)
                        .skip((page-1)*10),
                TeacherMessage.class);
        if (page == 1) {
            //更新用户的版本号
            mongoTemplate.updateFirst(query(Criteria.where("id").is(userId)),
                    new Update().set("version", list.get(0).getVersion()),
                    UserMsgRecord.class);
        }
        return list;
    }

    public Integer getDepartmentId(int userId) {
        return teacherMapper.selectDepartmentIdByUserId(userId);
    }

}
