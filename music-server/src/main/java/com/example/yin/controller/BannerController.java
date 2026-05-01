package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/13 13:16
 **/
@RequiredArgsConstructor
@RestController
@RequestMapping("/banner")
public class BannerController {

    private final BannerService bannerService;

    @GetMapping("/getAllBanner")
    public R getAllBanner(){
        return R.success("成功获取轮播图与",bannerService.getAllBanner());
    }
}
