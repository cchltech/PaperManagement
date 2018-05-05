package com.cchl;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Generator {
    public static void main(String[] args) {
        try {
            List<String> warnings = new ArrayList<>();
            InputStream inputStream = Generator.class.getResourceAsStream("/mybatis-generator.xml");
            ConfigurationParser configurationParser = new ConfigurationParser(warnings);
            Configuration configuration = configurationParser.parseConfiguration(inputStream);
            inputStream.close();
            DefaultShellCallback callback = new DefaultShellCallback(true);
            MyBatisGenerator generator = new MyBatisGenerator(configuration, callback, warnings);
            generator.generate(null);
            for (String warn:warnings)
                System.out.println(warn);
        } catch (IOException | XMLParserException | InvalidConfigurationException | SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
