package com.example.yin.controller;

import com.example.yin.common.Message;
import com.example.yin.constant.Constants;
import com.example.yin.domain.Singer;
import com.example.yin.service.impl.SingerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class SingerController {

    @Autowired
    private SingerServiceImpl singerService;

    private Message message = new Message();

    @Configuration
    public static class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/img/singerPic/**")
                    .addResourceLocations(Constants.SINGER_PIC_PATH);
        }
    }

    // 添加歌手
    @PostMapping("/singer/add")
    public Object addSinger(HttpServletRequest req) {
        String name = req.getParameter("name").trim();
        String sex = req.getParameter("sex").trim();
        String birth = req.getParameter("birth").trim();
        String location = req.getParameter("location").trim();
        String introduction = req.getParameter("introduction").trim();
        String pic = "/img/avatorImages/user.jpg";

        Singer singer = new Singer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setBirth(myBirth);
        singer.setLocation(location);
        singer.setIntroduction(introduction);
        singer.setPic(pic);

        boolean res = singerService.addSinger(singer);
        if (res) {
            return message.success("添加成功");
        } else {
            return message.error("添加失败");
        }
    }

    // 删除歌手
    @GetMapping("/singer/delete")
    public Object deleteSinger(HttpServletRequest req) {
        String id = req.getParameter("id");

        boolean res = singerService.deleteSinger(Integer.parseInt(id));
        if (res) {
            return message.success("删除成功");
        } else {
            return message.error("删除失败");
        }
    }

    // 返回所有歌手
    @GetMapping("/singer")
    public Object allSinger() {
        return message.success(null, singerService.allSinger());
    }

    // 根据歌手名查找歌手
    @GetMapping("/singer/name/detail")
    public Object singerOfName(HttpServletRequest req) {
        String name = req.getParameter("name").trim();

        return message.success(null, singerService.singerOfName(name));
    }

    // 根据歌手性别查找歌手
    @GetMapping("/singer/sex/detail")
    public Object singerOfSex(HttpServletRequest req) {
        String sex = req.getParameter("sex").trim();

        return message.success(null, singerService.singerOfSex(Integer.parseInt(sex)));
    }

    // 更新歌手信息
    @PostMapping("/singer/update")
    public Object updateSingerMsg(HttpServletRequest req) {
        String id = req.getParameter("id").trim();
        String name = req.getParameter("name").trim();
        String sex = req.getParameter("sex").trim();
        String birth = req.getParameter("birth").trim();
        String location = req.getParameter("location").trim();
        String introduction = req.getParameter("introduction").trim();

        Singer singer = new Singer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        singer.setId(Integer.parseInt(id));
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setBirth(myBirth);
        singer.setLocation(location);
        singer.setIntroduction(introduction);

        boolean res = singerService.updateSingerMsg(singer);
        if (res) {
            return message.success("修改成功");
        } else {
            return message.error("修改失败");
        }
    }

    // 更新歌手头像
    @PostMapping("/singer/avatar/update")
    public Object updateSingerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "singerPic";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String imgPath = "/img/singerPic/" + fileName;
        try {
            avatorFile.transferTo(dest);
            Singer singer = new Singer();
            singer.setId(id);
            singer.setPic(imgPath);

            boolean res = singerService.updateSingerPic(singer);
            if (res) {
                return message.success("上传成功", imgPath);
            } else {
                return message.error("上传失败");
            }
        } catch (IOException e) {
            return message.fatal("上传失败" + e.getMessage());
        }
    }
}
