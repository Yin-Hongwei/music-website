package com.example.yin.service.impl;

import com.example.yin.common.R;
import com.example.yin.dao.SingerMapper;
import com.example.yin.domain.Singer;
import com.example.yin.request.SingerRequest;
import com.example.yin.service.SingerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class SingerServiceImpl implements SingerService {

    @Autowired
    private SingerMapper singerMapper;

    @Override
    public R updateSingerMsg(SingerRequest updateSingerRequest) {
        Singer singer = new Singer();
        BeanUtils.copyProperties(updateSingerRequest, singer);
        if (singerMapper.updateSingerMsg(singer) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R updateSingerPic(MultipartFile avatorFile, int id) {
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "singerPic";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            if (!file1.mkdir()) {
                return R.fatal("创建文件失败");
            }
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String imgPath = "/img/singerPic/" + fileName;
        try {
            avatorFile.transferTo(dest);
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
        Singer singer = new Singer();
        singer.setId(id);
        singer.setPic(imgPath);

        if (singerMapper.updateSingerPic(singer) > 0) {
            return R.success("上传成功", imgPath);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R deleteSinger(Integer id) {
        if (singerMapper.deleteSinger(id) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R allSinger() {
        return R.success(null, singerMapper.allSinger());
    }

    @Override
    public R addSinger(SingerRequest addSingerRequest) {
        Singer singer = new Singer();
        String pic = "/img/avatorImages/user.jpg";
        BeanUtils.copyProperties(addSingerRequest, singer);
        singer.setPic(pic);
        if (singerMapper.insertSelective(singer) > 0) {
            return R.success("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @Override
    public List<Singer> singerOfName(String name) {
        return singerMapper.singerOfName(name);
    }

    @Override
    public R singerOfSex(Integer sex) {
        return R.success(null, singerMapper.singerOfSex(sex));
    }
}
