package com.yin.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.music.model.R;
import com.yin.music.mapper.SingerMapper;
import com.yin.music.mapper.SingerStyleMapper;
import com.yin.music.model.domain.Singer;
import com.yin.music.model.domain.SingerStyle;
import com.yin.music.model.request.SingerRequest;
import com.yin.music.model.response.PageResponse;
import com.yin.music.model.response.SingerResponse;
import com.yin.music.model.response.StyleOptionResponse;
import com.yin.music.config.MediaProperties;
import com.yin.music.model.response.UploadResponse;
import com.yin.music.service.SingerService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SingerServiceImpl extends ServiceImpl<SingerMapper, Singer> implements SingerService {

    private final SingerMapper singerMapper;

    private final SingerStyleMapper singerStyleMapper;

    private final MediaProperties mediaProperties;

    @Override
    public R<?> updateSingerMsg(SingerRequest updateSingerRequest) {
        Singer singer = new Singer();
        BeanUtils.copyProperties(updateSingerRequest, singer);
        normalizeSingerKindSex(singer);
        if (singer.getId() == null) {
            return R.error("修改失败");
        }
        UpdateWrapper<Singer> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", singer.getId())
                .set("name", singer.getName())
                .set("kind", singer.getKind())
                .set("sex", singer.getSex())
                .set("birth", singer.getBirth())
                .set("location", singer.getLocation())
                .set("introduction", singer.getIntroduction());
        if (singerMapper.update(null, wrapper) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R<?> updateSingerPic(MultipartFile avatorFile, int id) {
        // Singer pics are /img/singerPic/** (local media), same as seed data & WebMvcConfig.
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        Path folder = mediaProperties.imgDir("singerPic");
        try {
            Files.createDirectories(folder);
            avatorFile.transferTo(folder.resolve(fileName));
        } catch (IOException e) {
            return R.fatal("上传失败");
        }
        String imgPath = "/img/singerPic/" + fileName;
        Singer singer = new Singer();
        singer.setId(id);
        singer.setPic(imgPath);
        if (singerMapper.updateById(singer) > 0) {
            return R.success("上传成功", UploadResponse.of(imgPath));
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R<?> deleteSinger(Integer id) {
        if (singerMapper.deleteById(id) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R<?> allSinger() {
        return R.success(null, SingerResponse.fromList(singerMapper.selectList(null)));
    }

    @Override
    public R<?> pageSinger(Integer page, Integer size) {
        Page<Singer> pageInfo = new Page<>(page, size);
        singerMapper.selectPage(pageInfo, null);
        return R.success(null, PageResponse.of(
                SingerResponse.fromList(pageInfo.getRecords()),
                pageInfo.getTotal(),
                page,
                size
        ));
    }

    @Override
    public R<?> singerOfId(Integer id) {
        Singer singer = singerMapper.selectById(id);
        if (singer == null) {
            return R.error("歌手不存在");
        }
        return R.success(null, SingerResponse.from(singer));
    }

    @Override
    public R<?> addSinger(SingerRequest addSingerRequest) {
        Singer singer = new Singer();
        BeanUtils.copyProperties(addSingerRequest, singer);
        normalizeSingerKindSex(singer);
        String pic = "/img/avatarImages/user.jpg";
        singer.setPic(pic);
        if (singerMapper.insert(singer) > 0) {
            return R.success("添加成功", Collections.singletonMap("id", singer.getId()));
        } else {
            return R.error("添加失败");
        }
    }

    @Override
    public R<?> singerOfName(String name) {
        QueryWrapper<Singer> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        return R.success(null, SingerResponse.fromList(singerMapper.selectList(queryWrapper)));
    }

    /**
     * 兼容 singer_style.type 筛选项：0 女歌手 / 1 男歌手 / 2 组合。
     */
    @Override
    public R<?> singerOfType(Integer type) {
        QueryWrapper<Singer> queryWrapper = new QueryWrapper<>();
        if (type != null && type == 2) {
            queryWrapper.eq("kind", 1);
        } else if (type != null && (type == 0 || type == 1)) {
            queryWrapper.eq("kind", 0).eq("sex", type);
        } else if (type != null && type == 3) {
            queryWrapper.eq("kind", 2);
        } else {
            return R.success(null, Collections.emptyList());
        }
        return R.success(null, SingerResponse.fromList(singerMapper.selectList(queryWrapper)));
    }

    /** 组合/其他清空性别与出生日期；个人缺省类型为 0 */
    private void normalizeSingerKindSex(Singer singer) {
        Byte kind = singer.getKind();
        if (kind == null) {
            singer.setKind((byte) 0);
            kind = 0;
        }
        if (kind != 0) {
            singer.setSex(null);
            singer.setBirth(null);
        }
    }

    @Override
    public R<?> allSingerStyle() {
        List<StyleOptionResponse> styleList = new ArrayList<>();
        styleList.add(StyleOptionResponse.of(0, "全部歌手", -1));

        List<SingerStyle> dbStyles = singerStyleMapper.selectList(new QueryWrapper<SingerStyle>().orderByAsc("id"));
        for (SingerStyle item : dbStyles) {
            if (item == null || item.getName() == null || item.getName().trim().isEmpty() || item.getType() == null) {
                continue;
            }
            styleList.add(StyleOptionResponse.of(item.getId(), item.getName().trim(), item.getType()));
        }
        return R.success(null, styleList);
    }
}
