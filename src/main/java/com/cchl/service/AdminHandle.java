package com.cchl.service;

import com.cchl.dao.StudentMapper;
import com.cchl.dao.TitleMapper;
import com.cchl.dto.Result;
import com.cchl.eumn.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理员的事项处理
 */
@Service
public class AdminHandle {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TitleMapper titleMapper;

    /**
     * 查找未选题的学生和选题未满的题目
     * @param data 代表查找学生还是题目
     * @return
     */
    public Result allocate(String data) {

        if (data != null && "student".equals(data)) {
            return new Result<>(true, studentMapper.selectUnTitle());
        } else if (data != null && "title".equals(data)) {
            return new Result<>(true, titleMapper.selectUnFull());
        } else {
            return new Result(Dictionary.ILLEGAL_VISIT);
        }
    }
}
