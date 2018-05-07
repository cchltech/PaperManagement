package com.cchl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.cchl.dao")
public class PaperManagement {
    public static void main(String[] args) {
        SpringApplication.run(PaperManagement.class, args);
    }
}
