package com.cchl.service.student;

import com.cchl.entity.vo.StudentMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentHandleTest {

    @Autowired
    private StudentHandle studentHandle;

    @Test
    public void getTitleList() throws Exception {
    }

    @Test
    public void selectTitles() throws Exception {
    }

    @Test
    public void msgTest() throws Exception {
        int userId = 1;
        if (studentHandle.hasNewMsg(userId) > 0) {
            List<StudentMessage> list = studentHandle.getMsg(userId, 1);
            for (StudentMessage message : list)
                System.out.println(message.toString());
        }
    }

}