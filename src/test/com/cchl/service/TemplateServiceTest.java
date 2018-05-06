package com.cchl.service;

import com.cchl.eumn.DocType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TemplateServiceTest {

    @Autowired
    private TemplateService templateService;
    @Test
    public void getDoc() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("name", "chen");
        map.put("sex", "男");
        map.put("age", "23");
        templateService.getDoc(map, DocType.TASK, "201524133114");
    }

}