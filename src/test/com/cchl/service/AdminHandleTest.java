package com.cchl.service;

import com.cchl.entity.StudentMessage;
import com.cchl.entity.TeacherMessage;
import com.cchl.entity.Title;
import com.cchl.service.admin.AdminHandle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminHandleTest {

    @Autowired
    private AdminHandle adminHandle;

    @Test
    public void allocate() throws Exception {
        //output 3
//        adminHandle.allocate("student");
        List<Title> titles = (List<Title>) adminHandle.allocate("title").getData();
        for (Title title : titles)
            System.out.println(title.toString());
    }

    @Test
    public void AddMsgTest() throws Exception {
        for (int i = 0; i < 10; i++) {
            adminHandle.addMsg("student test" + i, 1001+i/3);
            adminHandle.addMsg("teacher test" + i, null);
        }
    }

    @Test
    public void selectMsgTest() throws Exception {
        List<StudentMessage> messages = adminHandle.selectStudentMsg(1);
        for (StudentMessage message : messages)
            System.out.println(message.toString());

        List<TeacherMessage> messages1 = adminHandle.selectTeacherMsg(1);
        for (TeacherMessage message : messages1)
            System.out.println(message.toString());
    }
}