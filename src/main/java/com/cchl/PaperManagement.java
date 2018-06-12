package com.cchl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.web.support.SpringBootServletInitializer;


@SpringBootApplication
@EnableCaching
@MapperScan(basePackages = "com.cchl.dao")
public class PaperManagement extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(PaperManagement.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PaperManagement.class, args);
    }
}
