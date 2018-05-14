package com.cchl.service;

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
        List<Title> titles = (List<Title>)adminHandle.allocate("title").getData();
        for (Title title:titles)
            System.out.println(title.toString());
    }

}