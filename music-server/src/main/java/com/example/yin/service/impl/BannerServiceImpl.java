package com.example.yin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.common.R;
import com.example.yin.constant.Constants;
import com.example.yin.mapper.BannerMapper;
import com.example.yin.model.domain.Banner;
import com.example.yin.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    @Cacheable(value = "banner", key = "'list'")  //放在缓存中 redis 是以key-value进行存储的
    @Override
    public List<Banner> getAllBanner() {
        return bannerMapper.selectList(null);
    }

    @CacheEvict(value = "banner", allEntries = true)
    @Override
    public R addBanner(MultipartFile bannerFile) {
        String fileName = System.currentTimeMillis() + bannerFile.getOriginalFilename();
        String filePath = Constants.PROJECT_PATH + System.getProperty("file.separator")
                + "img" + System.getProperty("file.separator") + "swiper";
        File folder = new File(filePath);
        if (!folder.exists() && !folder.mkdir()) {
            return R.fatal("创建目录失败");
        }
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String imgPath = "/img/swiper/" + fileName;
        try {
            bannerFile.transferTo(dest);
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }

        Banner banner = new Banner();
        banner.setPic(imgPath);
        banner.setUrl("");
        banner.setTitle("");
        if (bannerMapper.insert(banner) > 0) {
            return R.success("上传成功", banner);
        }
        return R.error("上传失败");
    }

    @CacheEvict(value = "banner", allEntries = true)
    @Override
    public R updateBannerPic(MultipartFile bannerFile, Integer id) {
        String fileName = System.currentTimeMillis() + bannerFile.getOriginalFilename();
        String filePath = Constants.PROJECT_PATH + System.getProperty("file.separator")
                + "img" + System.getProperty("file.separator") + "swiper";
        File folder = new File(filePath);
        if (!folder.exists() && !folder.mkdir()) {
            return R.fatal("创建目录失败");
        }
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String imgPath = "/img/swiper/" + fileName;
        try {
            bannerFile.transferTo(dest);
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }

        Banner banner = new Banner();
        banner.setId(id);
        banner.setPic(imgPath);
        if (bannerMapper.updateById(banner) > 0) {
            return R.success("更新成功", banner);
        }
        return R.error("更新失败");
    }

    @CacheEvict(value = "banner", allEntries = true)
    @Override
    public R updateBannerMeta(Integer id, String url, String title) {
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
    public R deleteBanner(Integer id) {
        if (bannerMapper.deleteById(id) > 0) {
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }
}
