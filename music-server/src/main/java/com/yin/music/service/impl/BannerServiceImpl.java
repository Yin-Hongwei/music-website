package com.yin.music.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.music.model.R;
import com.yin.music.config.MediaProperties;
import com.yin.music.mapper.BannerMapper;
import com.yin.music.model.domain.Banner;
import com.yin.music.model.response.BannerResponse;
import com.yin.music.service.BannerService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author asus
 * @description 针对表【banner】的数据库操作Service实现
 * @createDate 2022-06-13 13:13:42
 */
@RequiredArgsConstructor
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner>
        implements BannerService {

    private final BannerMapper bannerMapper;
    private final MediaProperties mediaProperties;

    @Cacheable(value = "banner", key = "'list'")  //放在缓存中 redis 是以key-value进行存储的
    @Override
    public List<Banner> getAllBanner() {
        return bannerMapper.selectList(null);
    }

    @CacheEvict(value = "banner", allEntries = true)
    @Override
    public R<?> addBanner(MultipartFile bannerFile) {
        String fileName = System.currentTimeMillis() + bannerFile.getOriginalFilename();
        Path folder = mediaProperties.imgDir("swiper");
        try {
            Files.createDirectories(folder);
            Path dest = folder.resolve(fileName);
            bannerFile.transferTo(dest);
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
        String imgPath = "/img/swiper/" + fileName;

        Banner banner = new Banner();
        banner.setPic(imgPath);
        banner.setUrl("");
        banner.setTitle("");
        if (bannerMapper.insert(banner) > 0) {
            return R.success("上传成功", BannerResponse.from(banner));
        }
        return R.error("上传失败");
    }

    @CacheEvict(value = "banner", allEntries = true)
    @Override
    public R<?> updateBannerPic(MultipartFile bannerFile, Integer id) {
        String fileName = System.currentTimeMillis() + bannerFile.getOriginalFilename();
        Path folder = mediaProperties.imgDir("swiper");
        try {
            Files.createDirectories(folder);
            Path dest = folder.resolve(fileName);
            bannerFile.transferTo(dest);
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
        String imgPath = "/img/swiper/" + fileName;

        Banner banner = new Banner();
        banner.setId(id);
        banner.setPic(imgPath);
        if (bannerMapper.updateById(banner) > 0) {
            return R.success("更新成功", BannerResponse.from(banner));
        }
        return R.error("更新失败");
    }

    @CacheEvict(value = "banner", allEntries = true)
    @Override
    public R<?> updateBannerMeta(Integer id, String url, String title) {
        Banner banner = new Banner();
        banner.setId(id);
        banner.setUrl(url == null ? "" : url.trim());
        banner.setTitle(title == null ? "" : title.trim());
        if (bannerMapper.updateById(banner) > 0) {
            return R.success("更新成功");
        }
        return R.error("更新失败");
    }

    @CacheEvict(value = "banner", allEntries = true)
    @Override
    public R<?> deleteBanner(Integer id) {
        if (bannerMapper.deleteById(id) > 0) {
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }
}
