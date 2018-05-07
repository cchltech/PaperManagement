package com.cchl.service;

import com.cchl.entity.Student;
import com.cchl.service.Impl.StudentLoginServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginServiceTest {

    @Autowired
    @Qualifier(value = "student")
    private LoginService studentService;

    @Test
    public void loginCheck() throws Exception {
        System.out.println(studentService.loginCheck("201524133114", "123456"));
    }

    @Test
    public void studentRegister() throws Exception {
        Student student = new Student();
        student.setId(201524133115L);
        student.setName("chen");
        student.setPassword("123456");
        student.setSex((byte)1);
        student.setEmail("10010@qq.com");
        student.setDepartmentId(1001);
        student.setGrade((byte)15);
        student.setMajorId(10012);
        student.setPhone(17876253455L);
        System.out.println(studentService.studentRegister(student));
    }

    @Test
    public void teacherRegister() throws Exception {
    }

}