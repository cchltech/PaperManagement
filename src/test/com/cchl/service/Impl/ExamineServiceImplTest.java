package com.cchl.service.Impl;

import com.cchl.entity.Title;
import com.cchl.entity.User;
import com.cchl.service.admin.ExamineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExamineServiceImplTest {

    @Autowired
    private ExamineService examineService;


    @Test
    public void users() throws Exception {
        List<User> results = examineService.users(1000,0,10);
        for (User user:results) {
            System.out.println(user);
        }
    }

    @Test
    public void resultUser() throws Exception {
        Integer[] result = examineService.resultUser(new Integer[]{1},new Byte[]{1});
        for (int re:result){
            System.out.println(re);
        }
    }



    @Test
    public void resultTitle() throws Exception {
        Integer[] result = examineService.resultTitle(new Integer[]{1},new Byte[]{1});
        for (int re:result){
            System.out.println(re);
        }
    }

}