package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.model.domain.Banner;

import java.util.List;

/**
* @author asus
* @description 针对表【banner】的数据库操作Service
* @createDate 2022-06-13 13:13:42
*/
public interface BannerService extends IService<Banner> {

    List<Banner> getAllBanner();

}
