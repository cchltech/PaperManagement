package com.cchl.service;

import com.cchl.entity.Title;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class TitleHandleTest {

    @Autowired
    private TitleHandle titleHandle;

    @Test
    public void insert() throws Exception {
        Title title = new Title();
        title.setContent("test");
        title.setInstruction("test");
        title.setStatus((byte)0);
        System.out.println(titleHandle.insert(title));
    }

}