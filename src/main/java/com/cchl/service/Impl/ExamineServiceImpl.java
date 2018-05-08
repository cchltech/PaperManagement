package com.cchl.service.Impl;

import com.cchl.dao.TitleMapper;
import com.cchl.dao.UserMapper;
import com.cchl.entity.Title;
import com.cchl.entity.User;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.SystemException;
import com.cchl.service.ExamineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamineServiceImpl implements ExamineService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TitleMapper titleMapper;

    @Override
    public int totalNumber(int type) {
        int result = 0;
        switch (type) {
            case 0: result = userMapper.totalNumber(false, (byte)0); break;
            case 1: result = titleMapper.totalNumber(false, (byte)0); break;
            default:break;
        }
        return result;
    }

    @Override
    public List<User> users(int page, int limit) {
        try {
            return userMapper.selectUnaudited(page, limit);
        } catch (Exception e) {
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
