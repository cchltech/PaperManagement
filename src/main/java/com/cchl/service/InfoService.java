package com.cchl.service;

import com.cchl.dao.TeacherMapper;
import com.cchl.dao.TitleMapper;
import com.cchl.entity.Title;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.IllegalVisitException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公共信息展示处理
 */
@Service
public class InfoService {

    @Autowired
    private TitleMapper titleMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    public List<Title> titles(int page, int limit, int userId) {
        //查找对应的学院id
        Integer departmentId = selectDepartmentId(userId);
        if (departmentId == null)
            throw new IllegalVisitException(Dictionary.ILLEGAL_VISIT);
        return titleMapper.selectByStatus((page - 1) * limit, limit, 1, false, departmentId);
    }

    public int totalNumber(int type, int userId) {
        //查找类型，题目为0
        int result = 0;
        switch (type) {
            case 0:
                result = titleMapper.totalNumber(false, (byte) 1, selectDepartmentId(userId));
                break;
            default:
                break;
        }
        return result;
    }

    @Cacheable(value = "userId")
    public Integer selectDepartmentId(int userId) {
        return teacherMapper.selectDepartmentIdByUserId(userId);
    }
}
