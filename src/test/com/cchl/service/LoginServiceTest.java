package com.cchl.service;

import com.cchl.entity.Student;
import com.cchl.entity.Teacher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginServiceTest {

    @Autowired
    @Qualifier(value = "student")
    private LoginService studentService;

    @Autowired
    @Qualifier(value = "teacher")
    private LoginService teacherService;

    @Test
    public void loginCheck() throws Exception {
        System.out.println(studentService.loginCheck("201524133114", "123456"));
    }

    @Test
    public void studentRegister() throws Exception {
        Student student = new Student();
        student.setId(201524131001L);
        student.setName("snow");
        student.setPassword("123456");
        student.setSex((byte)0);
        student.setEmail("10010@qq.com");
        student.setDepartmentId(1002);
        student.setGrade((byte)16);
        student.setMajorId(10021);
        student.setPhone(17876250123L);
        System.out.println(studentService.studentRegister(student));
    }

    @Test
    public void teacherRegister() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(201001011L);
        teacher.setName("Mr.Wu");
        teacher.setPassword("123456");
        teacher.setSex((byte)0);
        teacher.setDepartmentId(1001);
        teacher.setPhone(10086L);
        System.out.println(teacherService.teacherRegister(teacher));
    }

}