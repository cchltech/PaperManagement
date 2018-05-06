package com.cchl.service;

import com.cchl.eumn.DocType;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.Map;

/**
 * 创建模板文件
 */
@Service
public class TemplateService {

    //文件路径
    private final static String FILE_PATH = "/home/beiyi/MyFile/project2/";
    //注入FreemarkerConfigurer
    @Autowired
    private FreeMarkerConfigurer configurer;

    /**
     * 生成doc文件
     * @param map 参数
     * @param docType 模板类型
     * @param id 调用者的论文计划id
     */
    public void getDoc(Map<String, String> map, DocType docType, String id) {
        try {
            Configuration configuration = configurer.getConfiguration();
            configuration.setDefaultEncoding("utf-8");
            configuration.setClassForTemplateLoading(TemplateService.class, "/template/");
            File file = new File(FILE_PATH + '/' + id + '/' + docType.getType() + ".doc");
            File file1 = file.getParentFile();
            //如果文件路径不存在就创建文件
            if (!file1.exists())
                file1.mkdirs();
            file.createNewFile();

            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
            configuration.getTemplate(docType.getType() + ".ftl").process(map, writer);
            writer.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
