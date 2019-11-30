package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.Singer;
import com.example.demo.service.impl.SingerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
@Controller
public class SingerController {

    @Autowired
    private SingerServiceImpl singerService;
//    添加歌手
    @ResponseBody
    @RequestMapping(value = "/api/addSinger", method = RequestMethod.POST)
    public Object addSinger(HttpServletRequest req){
        JSONObject jsonObject = new JSONObject();
        String name = req.getParameter("name").trim();
        String sex = req.getParameter("sex").trim();
        String pic = req.getParameter("pic").trim();
        String birth = req.getParameter("birth").trim();
        String location = req.getParameter("location").trim();
        String introduction = req.getParameter("introduction").trim();

        Singer singer = new Singer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        }catch (Exception e){
            e.printStackTrace();
        }
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setPic(pic);
        singer.setBirth(myBirth);
        singer.setLocation(location);
        singer.setIntroduction(introduction);

        boolean res = singerService.ifAdd(singer);
        if (res){
            jsonObject.put("code", 1);
            jsonObject.put("msg", "添加成功");
            return jsonObject;
        }else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "添加失败");
            return jsonObject;
        }
    }

//    删除歌手
    @RequestMapping(value = "/api/deleteSingers", method = RequestMethod.GET)
    public Object deleteSingers(HttpServletRequest req){
        String id = req.getParameter("id");
        return singerService.deleteSinger(Integer.parseInt(id));
    }

//    更新歌手信息
    @ResponseBody
    @RequestMapping(value = "/api/updateSingerMsgs", method = RequestMethod.POST)
    public Object updateSingerMsgs(HttpServletRequest req){
        JSONObject jsonObject = new JSONObject();
        String id = req.getParameter("id").trim();
        String name = req.getParameter("name").trim();
        String sex = req.getParameter("sex").trim();
        String pic = req.getParameter("pic").trim();
        String birth = req.getParameter("birth").trim();
        String location = req.getParameter("location").trim();
        String introduction = req.getParameter("introduction").trim();

        Singer singer = new Singer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        }catch (Exception e){
            e.printStackTrace();
        }
        singer.setId(Integer.parseInt(id));
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setPic(pic);
        singer.setBirth(myBirth);
        singer.setLocation(location);
        singer.setIntroduction(introduction);

        boolean res = singerService.updateSingerMsg(singer);
        if (res){
            jsonObject.put("code", 1);
            jsonObject.put("msg", "修改成功");
            return jsonObject;
        }else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "修改失败");
            return jsonObject;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/api/updateSingerImg", method = RequestMethod.POST)
    public Object updateSingerImg(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id")int id){
        JSONObject jsonObject = new JSONObject();

        if (avatorFile.isEmpty()) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "文件上传失败！");
            return jsonObject;
        }
        String fileName = System.currentTimeMillis()+avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "singerPic" ;
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeAvatorPath = "/img/singerPic/"+fileName;
        try {
            avatorFile.transferTo(dest);
            Singer singer = new Singer();
            singer.setId(id);
            singer.setPic(storeAvatorPath);
            boolean res = singerService.updateSingerImg(singer);
            if (res){
                jsonObject.put("code", 1);
                jsonObject.put("pic", storeAvatorPath);
                jsonObject.put("msg", "上传成功");
                return jsonObject;
            }else {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "上传失败");
                return jsonObject;
            }
        }catch (IOException e){
            jsonObject.put("code", 0);
            jsonObject.put("msg", "上传失败" + e.getMessage());
            return jsonObject;
        }finally {
            return jsonObject;
        }
    }

    @Configuration
    public class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/static/singerPic/**").addResourceLocations("file:/Users/yhw/Documents/github-workspace/music-website/music-server/img/singerPic/");
        }
    }

    //    返回所有歌手
    @RequestMapping(value = "/listSingers", method = RequestMethod.GET)
    public Object toSingerList(){
        return singerService.listSingers();
    }

    //    根据歌手名查找歌手
    @RequestMapping(value = "/searachSingers", method = RequestMethod.GET)
    public Object searachSingers(HttpServletRequest req){
        String name = req.getParameter("name").trim();
        return singerService.searachSinger(name);
    }

    //    根据歌手性别查找歌手
    @RequestMapping(value = "/api/singer", method = RequestMethod.GET)
    public Object SingerSex(HttpServletRequest req){
        String sex = req.getParameter("sex").trim();
        return singerService.singerSex(Integer.parseInt(sex));
    }
}
