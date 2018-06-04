package com.cchl.service;

import com.cchl.entity.vo.AdminMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import static org.springframework.data.mongodb.core.query.Query.query;

import static org.springframework.data.mongodb.core.query.Update.update;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 全局消息控制类
 */
@Service
public class MsgHandle {

    private Logger logger = LoggerFactory.getLogger(MsgHandle.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 添加一条管理院消息
     *
     * @param type
     * @param departmentId
     */
    public void addAdminMsg(AdminMsg.TYPE type, int departmentId) {
        try {
            Criteria criteria = Criteria.where("type").is(type.getType()).and("departmentId").is(departmentId);
            List<AdminMsg> adminMsg = mongoTemplate.find(query(criteria), AdminMsg.class);
            if (adminMsg != null && adminMsg.size() > 0) {
                int number = adminMsg.get(0).getNumber();
                mongoTemplate.updateFirst(query(criteria), update("number", ++number), AdminMsg.class);
            } else {
                mongoTemplate.insert(new AdminMsg(type, 1, departmentId));
            }
        } catch (Exception e) {
            logger.error("添加管理员消息失败，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 重置指定类型的消息
     *
     * @param type
     * @param departmentId
     */
    public void resetAdminMsg(AdminMsg.TYPE type, int departmentId) {
        try {
            Criteria criteria = Criteria.where("type").is(type.getType()).and("departmentId").is(departmentId);
            List<AdminMsg> adminMsg = mongoTemplate.find(query(criteria), AdminMsg.class);
            if (adminMsg != null) {
                mongoTemplate.updateFirst(query(criteria), update("number", 0), AdminMsg.class);
            } else {
                mongoTemplate.insert(new AdminMsg(type, 0, departmentId));
            }
        } catch (Exception e) {
            logger.error("重置管理员消息失败，失败信息：{}", e.getMessage());
        }
    }

    public List<AdminMsg> getAdminMsg(Integer departmentId) {
        if (departmentId == null)
            return null;
        try {
            Criteria criteria = Criteria.where("departmentId").is(departmentId);
            return mongoTemplate.find(query(criteria), AdminMsg.class);
        } catch (Exception e) {
            logger.error("获取管理员消息失败，失败信息：{}", e.getMessage());
            return null;
        }
    }

}
