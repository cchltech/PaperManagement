package com.cchl.service.admin.Ipml;

import com.cchl.dao.TitleMapper;
import com.cchl.dao.UserMapper;
import com.cchl.entity.Title;
import com.cchl.entity.User;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.SystemException;
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

    @Override
    public int totalNumber(int type) {
        int result = 0;
        switch (type) {
            case 0:
                result = userMapper.totalNumber(false, (byte) 0);
                break;
            case 1:
                result = titleMapper.totalNumber(false, (byte) 0);
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public List<User> users(int page, int limit) {
        try {
            List<User> users = userMapper.selectUnaudited(page, limit);
            for (User user : users) {
                user.happyGive();
                logger.info(user.toString());
            }
            return users;
        } catch (Exception e) {
            logger.error("异常信息：{}", e.getMessage());
            throw new SystemException(Dictionary.SYSTEM_ERROR);
        }
    }

    @Override
    public Integer[] resultUser(Integer[] id, Byte[] status) {
        try {
            Integer[] result = new Integer[status.length];
            for (int i = 0; i < status.length; i++) {
                result[i] = userMapper.updateStatus(id[i], status[i]);
            }
            return result;
        } catch (Exception e) {
            throw new SystemException(Dictionary.SYSTEM_ERROR);
        }
    }

    @Override
    public List<Title> title(int page, int limit) {
        try {
            return titleMapper.selectUnaudited(page, limit);
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
}
