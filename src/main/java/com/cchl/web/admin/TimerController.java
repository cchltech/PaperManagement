package com.cchl.web.admin;

import com.cchl.dto.DataWithPage;
import com.cchl.dto.Result;
import com.cchl.entity.vo.VoTimer;
import com.cchl.eumn.Dictionary;
import com.cchl.eumn.TimerType;
import com.cchl.service.admin.AdminHandle;
import com.cchl.service.teacher.TeacherHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/timer")
public class TimerController {

    @Autowired
    private TeacherHandle teacherHandle;
    @Autowired
    private AdminHandle adminHandle;


    /**
     * 添加定时任务
     *
     * @param target  定时任务面向的用户类型，0：学生  1：教师
     * @param content 定时任务说明
     * @param type    定时任务类型
     * @param begin   开始时间
     * @param end     结束时间
     * @return
     */
    @PostMapping(value = "/add/{target}")
    public Result addTimer(@PathVariable(value = "target") int target,
                           @RequestParam(value = "type") int type,
                           @RequestParam(value = "content") String content,
                           @RequestParam(value = "begin") String begin,
                           @RequestParam(value = "end") String end,
                           @SessionAttribute(value = "user_id", required = false) Integer userId) {
        try {
            TimerType timerType = TimerType.stateOf(type);
            //TODO 测试账号
            int department = adminHandle.getDepartmentIdByUserId(userId);
            if (timerType != null) {
                VoTimer timer = new VoTimer(target, timerType, content, begin, end, department);
                adminHandle.addTimer(timer);
                return new Result(Dictionary.SUCCESS);
            }
            return new Result(Dictionary.ILLEGAL_VISIT);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    @PostMapping(value = "/update")
    public Result updateTimer(@RequestParam(value = "id")Integer id,
                              @RequestParam(value = "content") String content,
                              @RequestParam(value = "begin") String begin,
                              @RequestParam(value = "end") String end,
                              @SessionAttribute(value = "user_id", required = false) Integer userId) {
        try {
            int department = adminHandle.getDepartmentIdByUserId(userId);
            adminHandle.updateTimer(id, content, begin, end);
            return new Result(Dictionary.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

    @GetMapping(value = "/show")
    public DataWithPage show(@SessionAttribute(value = "user_id", required = false) Integer userId,
                             @RequestParam(value = "page", required = false) Integer page,
                             @RequestParam(value = "limit", required = false) Integer limit) {
        try {
            if (page == null || limit == null) {
                page = 1;
                limit = 10;
            }
            int department = adminHandle.getDepartmentIdByUserId(userId);
            return new DataWithPage<>(0, adminHandle.TimerCount(department), adminHandle.selectByDepartmentId(department, (page-1)*10, limit));
        } catch (Exception e) {
            e.printStackTrace();
            return new DataWithPage(Dictionary.SYSTEM_ERROR);
        }
    }

    @PostMapping(value = "/remove")
    public Result remove(@RequestParam(value = "id")Integer id) {
        try {
            adminHandle.removeTimer(id);
            return new Result(Dictionary.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Dictionary.SYSTEM_ERROR);
        }
    }

}
