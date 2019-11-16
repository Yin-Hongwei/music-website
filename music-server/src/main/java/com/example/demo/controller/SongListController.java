package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.SongList;
import com.example.demo.service.impl.SongListServiceImpl;
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


@RestController
@Controller
public class SongListController {

    @Autowired
    private SongListServiceImpl songListService;

//    添加歌单
@ResponseBody
@RequestMapping(value = "/api/addSongList", method = RequestMethod.POST)
public Object addSongList(HttpServletRequest req){
    JSONObject jsonObject = new JSONObject();
    String title = req.getParameter("title").trim();
    String pic = req.getParameter("pic").trim();
    String introduction = req.getParameter("introduction").trim();
    String style = req.getParameter("style").trim();

    SongList songList = new SongList();
    songList.setTitle(title);
    songList.setPic(pic);
    songList.setIntroduction(introduction);
    songList.setStyle(style);

    boolean res = songListService.ifAdd(songList);
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

//    删除歌单
    @RequestMapping(value = "/api/deleteSongLists", method = RequestMethod.GET)
    public Object deleteSongLists(HttpServletRequest req){
        String id = req.getParameter("id");
        return songListService.deleteSongList(Integer.parseInt(id));
    }

//    更新歌单信息
    @ResponseBody
    @RequestMapping(value = "/api/updateSongListMsgs", method = RequestMethod.POST)
    public Object updateSongListMsgs(HttpServletRequest req){
        JSONObject jsonObject = new JSONObject();
        String id = req.getParameter("id").trim();
        String title = req.getParameter("title").trim();
        String pic = req.getParameter("pic").trim();
        String introduction = req.getParameter("introduction").trim();
        String style = req.getParameter("style").trim();

        SongList songList = new SongList();
        songList.setId(Integer.parseInt(id));
        songList.setTitle(title);
        songList.setPic(pic);
        songList.setIntroduction(introduction);
        songList.setStyle(style);

        boolean res = songListService.updateSongListMsg(songList);
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

    //    更新歌单图片
    @ResponseBody
    @RequestMapping(value = "/api/updateSongListImg", method = RequestMethod.POST)
    public Object updateSongListImg(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id")int id){
        JSONObject jsonObject = new JSONObject();

        if (avatorFile.isEmpty()) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "文件上传失败！");
            return jsonObject;
        }
        String fileName = System.currentTimeMillis()+avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "songListPic" ;
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeAvatorPath = "/img/songListPic/"+fileName;
        try {
            avatorFile.transferTo(dest);
            SongList songList = new SongList();
            songList.setId(id);
            songList.setPic(storeAvatorPath);
            boolean res = songListService.updateSongListImg(songList);
            if (res){
                jsonObject.put("code", 1);
                jsonObject.put("avator", storeAvatorPath);
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
            registry.addResourceHandler("/img/songListPic/**").addResourceLocations("file:/Users/yhw/Documents/github-workspace/music-website/music-server/img/songListPic/");
        }
    }


    @RequestMapping(value = "/api/songAlbum", method = RequestMethod.GET)
    public Object songAlbum(HttpServletRequest req){
        String title = req.getParameter("title").trim();
        return songListService.songAlbum(title);
    }

    //    返回所有歌单
    @RequestMapping(value = "/listSongLists", method = RequestMethod.GET)
    public Object toSongList(){
        return songListService.listSongLists();
    }

}















