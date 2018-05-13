package com.cchl.web.admin;

import com.cchl.dao.StudentMapper;
import com.cchl.dto.Result;
import com.cchl.eumn.Dictionary;
import com.cchl.execption.IllegalVisitException;
import com.cchl.service.AdminHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 管理员为未选到体的学生进行题目调配
 */
@Controller
@RequestMapping(value = "/allocate")
public class AllocateController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminHandle adminHandle;

    /**
     * @return 跳转到初始页面
     */
    @RequestMapping(value = "/index")
    public String index() {
        return "allocation";
    }

    @RequestMapping(value = "/index/{data}")
    public Result getData(@PathVariable(value = "data") String data) {
        try {
            logger.info("调课操作，选择的数据为：{}", data);
            return adminHandle.allocate(data);
        } catch (Exception e) {
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }
}
