package com.cchl.service.admin;

import com.cchl.entity.GroupInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GroupingHandleTest {

    @Autowired
    private GroupingHandle groupingHandle;

    @Test
    public void getGroupList() throws Exception {
        System.out.println(groupingHandle.getFile(123456, 0));
    }

}