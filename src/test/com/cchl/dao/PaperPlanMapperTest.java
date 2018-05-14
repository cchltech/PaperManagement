package com.cchl.dao;

import com.cchl.entity.PaperPlan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PaperPlanMapperTest {

    @Autowired
    private PaperPlanMapper paperPlanMapper;

    @Test
    public void insert() throws Exception {
        PaperPlan paperPlan = new PaperPlan();
        paperPlan.setTitleId(1);
        paperPlanMapper.insert(paperPlan);
        System.out.println(paperPlan.toString());
    }

}