package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.domain.Banner;
import com.example.yin.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/add")
    public R addBanner(@RequestParam("file") MultipartFile bannerFile) {
        return bannerService.addBanner(bannerFile);
    }

    @PostMapping("/updatePic")
    public R updateBannerPic(@RequestParam("file") MultipartFile bannerFile, @RequestParam("id") Integer id) {
        return bannerService.updateBannerPic(bannerFile, id);
    }

    @PostMapping("/updateUrl")
    public R updateBannerUrl(@RequestBody Banner banner) {
        if (banner == null || banner.getId() == null) {
            return R.error("参数错误");
        }
        return bannerService.updateBannerMeta(banner.getId(), banner.getUrl(), banner.getTitle());
    }

    @GetMapping("/delete")
    public R deleteBanner(@RequestParam("id") Integer id) {
        return bannerService.deleteBanner(id);
    }
}
