package com.textbook;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.textbook.mapper")
public class TextbookTradingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TextbookTradingApplication.class, args);
        System.out.println("===========================================");
        System.out.println("    二手教材交易系统启动成功!");
        System.out.println("    接口文档: http://localhost:8080/doc.html");
        System.out.println("===========================================");
    }
}
