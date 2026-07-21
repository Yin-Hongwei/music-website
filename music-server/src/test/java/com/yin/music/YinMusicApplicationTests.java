package com.yin.music;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.yin.music.service.impl.AppUserServiceImpl;

@SpringBootTest
public class YinMusicApplicationTests {

    @Autowired
    private AppUserServiceImpl appUserService;

    @Test
    public void appUserListSmokeTest() {
        System.out.println(appUserService.allUser());
    }
}
