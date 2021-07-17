package com.example.yin.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.yin.domain.Song;
import com.example.yin.service.impl.SongServiceImpl;
import com.example.yin.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@RestController
@Controller
public class SongController {

    @Autowired
    private SongServiceImpl songService;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大10M,DataUnit提供5中类型B,KB,MB,GB,TB
        factory.setMaxFileSize(DataSize.of(10, DataUnit.MEGABYTES));
        /// 设置总上传数据总大小10M
        factory.setMaxRequestSize(DataSize.of(10, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }

    @Configuration
    public class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) { // windos系统
                registry.addResourceHandler("/img/songPic/**")
                        .addResourceLocations("file:" + Constants.RESOURCE_WIN_PATH + "\\img\\songPic\\");
                registry.addResourceHandler("/song/**")
                        .addResourceLocations("file:" + Constants.RESOURCE_WIN_PATH + "\\song\\");
            } else { // MAC、Linux系统
                registry.addResourceHandler("/img/songPic/**")
                        .addResourceLocations("file:" + Constants.RESOURCE_MAC_PATH + "/img/songPic/");
                registry.addResourceHandler("/song/**")
                        .addResourceLocations("file:" + Constants.RESOURCE_MAC_PATH + "/song/");
            }
        }
    }

//    添加歌曲
    @ResponseBody
    @RequestMapping(value = "/song/add", method = RequestMethod.POST)
    public Object addSong(HttpServletRequest req, @RequestParam("file") MultipartFile mpfile){
        JSONObject jsonObject = new JSONObject();
        String singer_id = req.getParameter("singerId").trim();
        String name = req.getParameter("name").trim();
        String introduction = req.getParameter("introduction").trim();
        String pic = "/img/songPic/tubiao.jpg";
        String lyric = req.getParameter("lyric").trim();

        if (mpfile.isEmpty()) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "音乐上传失败！");
            return jsonObject;
        }
        String fileName = mpfile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song";
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/song/"+fileName;
        try {
            mpfile.transferTo(dest);
            Song song = new Song();
            song.setSingerId(Integer.parseInt(singer_id));
            song.setName(name);
            song.setIntroduction(introduction);
            song.setCreateTime(new Date());
            song.setUpdateTime(new Date());
            song.setPic(pic);
            song.setLyric(lyric);
            song.setUrl(storeUrlPath);
            boolean res = songService.addSong(song);
            if (res) {
                jsonObject.put("code", 1);
                jsonObject.put("avator", storeUrlPath);
                jsonObject.put("msg", "上传成功");
                return jsonObject;
            } else {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "上传失败");
                return jsonObject;
            }
        } catch (IOException e) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "上传失败" + e.getMessage());
            return jsonObject;
        } finally {
            return jsonObject;
        }
    }

//    返回所有歌曲
    @RequestMapping(value = "/song", method = RequestMethod.GET)
    public Object allSong(){
        return songService.allSong();
    }

//    返回指定歌曲ID的歌曲
    @RequestMapping(value = "/song/detail", method = RequestMethod.GET)
    public Object songOfId(HttpServletRequest req){
        String id = req.getParameter("id");
        return songService.songOfId(Integer.parseInt(id));
    }

//    返回指定歌手ID的歌曲
    @RequestMapping(value = "/song/singer/detail", method = RequestMethod.GET)
    public Object songOfSingerId(HttpServletRequest req){
        String singerId = req.getParameter("singerId");
        return songService.songOfSingerId(Integer.parseInt(singerId));
    }

//    返回指定歌手名的歌曲
    @RequestMapping(value = "/song/singerName/detail", method = RequestMethod.GET)
    public Object songOfSingerName(HttpServletRequest req){
        String name = req.getParameter("name");
        return songService.songOfSingerName('%'+ name + '%');
    }

//    返回指定歌曲名的歌曲
    @RequestMapping(value = "/song/name/detail", method = RequestMethod.GET)
    public Object songOfName(HttpServletRequest req){
        String name = req.getParameter("name").trim();
        return songService.songOfName(name);
    }

//    删除歌曲
    @RequestMapping(value = "/song/delete", method = RequestMethod.GET)
    public Object deleteSong(HttpServletRequest req){
        String id = req.getParameter("id");
        return songService.deleteSong(Integer.parseInt(id));
    }

//    更新歌曲信息
    @ResponseBody
    @RequestMapping(value = "/song/update", method = RequestMethod.POST)
    public Object updateSongMsg(HttpServletRequest req){
        JSONObject jsonObject = new JSONObject();
        String id = req.getParameter("id").trim();
        String singer_id = req.getParameter("singerId").trim();
        String name = req.getParameter("name").trim();
        String introduction = req.getParameter("introduction").trim();
        String lyric = req.getParameter("lyric").trim();

        Song song = new Song();
        song.setId(Integer.parseInt(id));
        song.setSingerId(Integer.parseInt(singer_id));
        song.setName(name);
        song.setIntroduction(introduction);
        song.setUpdateTime(new Date());
        song.setLyric(lyric);

        boolean res = songService.updateSongMsg(song);
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

//    更新歌曲图片
    @ResponseBody
    @RequestMapping(value = "/song/img/update", method = RequestMethod.POST)
    public Object updateSongPic(@RequestParam("file") MultipartFile urlFile, @RequestParam("id")int id){
        JSONObject jsonObject = new JSONObject();

        if (urlFile.isEmpty()) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "音乐上传失败！");
            return jsonObject;
        }
        String fileName = System.currentTimeMillis()+urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "songPic";
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/img/songPic/"+fileName;
        try {
            urlFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setPic(storeUrlPath);
            boolean res = songService.updateSongPic(song);
            if (res){
                jsonObject.put("code", 1);
                jsonObject.put("avator", storeUrlPath);
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

//    更新歌曲URL
    @ResponseBody
    @RequestMapping(value = "/song/url/update", method = RequestMethod.POST)
    public Object updateSongUrl(@RequestParam("file") MultipartFile urlFile, @RequestParam("id")int id){
        JSONObject jsonObject = new JSONObject();

        if (urlFile.isEmpty()) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "音乐上传失败！");
            return jsonObject;
        }
        String fileName = urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song";
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/song/"+fileName;
        try {
            urlFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setUrl(storeUrlPath);
            boolean res = songService.updateSongUrl(song);
            if (res){
                jsonObject.put("code", 1);
                jsonObject.put("avator", storeUrlPath);
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
}
