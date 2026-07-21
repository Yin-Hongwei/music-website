package com.yin.music;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.yin.music.config.MediaProperties;

@SpringBootApplication
@MapperScan("com.yin.music.mapper")
@EnableConfigurationProperties(MediaProperties.class)
public class YinMusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(YinMusicApplication.class, args);
    }

}

