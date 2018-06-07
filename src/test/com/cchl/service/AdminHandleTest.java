package com.cchl.service;

import com.cchl.entity.vo.FileInfo;
import com.cchl.entity.vo.StudentMessage;
import com.cchl.entity.vo.TeacherMessage;
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
        List<Title> titles = (List<Title>) adminHandle.allocate("title", 1000).getData();
        for (Title title : titles)
            System.out.println(title.toString());
    }

    @Test
    public void AddMsgTest() throws Exception {
        for (int i = 10; i < 13; i++) {
//            adminHandle.addMsg("student test" + i, 1001+i/3);
            adminHandle.addMsg(1,"teacher test" + i, null);
        }
    }

    @Test
    public void selectMsgTest() throws Exception {
        List<StudentMessage> messages = adminHandle.selectStudentMsg(1, 10);
        for (StudentMessage message : messages)
            System.out.println(message.toString());

        List<TeacherMessage> messages1 = adminHandle.selectTeacherMsg(1, 10);
        for (TeacherMessage message : messages1)
            System.out.println(message.toString());
    }

    /**
     * 测试删除指定的消息
     * @throws Exception
     */
    @Test
    public void deleteMsgTest() throws Exception {

        //学生
        //学生需要指定学院以及版本号
        //adminHandle.deleteStudentMsg(1003, 3);

        //教师
        adminHandle.deleteTeacherMsg(1001,10);
    }

    @Test
    public void fileList() {
        List<FileInfo> list = adminHandle.openFileInfoList(1000, 0, 10);
        for (FileInfo fileInfo : list) {
            System.out.println(fileInfo);
        }
        System.out.println(adminHandle.openCount(1000));
    }


}