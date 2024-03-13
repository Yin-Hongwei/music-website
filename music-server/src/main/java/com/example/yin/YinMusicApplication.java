package com.example.yin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.example.yin.constant.Constants.ASSETS_PATH;

@SpringBootApplication
@MapperScan("com.example.yin.mapper")
public class YinMusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(YinMusicApplication.class, args);
    }

}

