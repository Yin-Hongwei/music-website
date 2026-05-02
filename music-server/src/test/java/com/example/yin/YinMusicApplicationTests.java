package com.example.yin;

import com.example.yin.service.impl.AppUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class YinMusicApplicationTests {

    @Autowired
    private AppUserServiceImpl appUserService;

    @Test
    public void appUserListSmokeTest()
    {
    System.out.println(appUserService.allUser());
    }
}
