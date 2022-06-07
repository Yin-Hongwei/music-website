package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.domain.SongList;
import com.example.yin.service.SongListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@RestController
public class SongListController {

    @Autowired
    private SongListService songListService;


    // 添加歌单
    @PostMapping("/songList/add")
    public R addSongList(HttpServletRequest req) {
        String title = req.getParameter("title").trim();
        String introduction = req.getParameter("introduction").trim();
        String style = req.getParameter("style").trim();
        String pic = "/img/songListPic/123.jpg";

        SongList songList = new SongList();
        songList.setTitle(title);
        songList.setIntroduction(introduction);
        songList.setStyle(style);
        songList.setPic(pic);

        boolean res = songListService.addSongList(songList);
        if (res) {
            return R.success("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    // 删除歌单
    @GetMapping("/songList/delete")
    public R deleteSongList(HttpServletRequest req) {
        String id = req.getParameter("id");

        boolean res = songListService.deleteSongList(Integer.parseInt(id));
        if (res) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    //TODO 这块就是前端显现相应的歌单list
    // 返回所有歌单
    @GetMapping("/songList")
    public R allSongList() {
        return songListService.allSongList();
    }

    // 返回标题包含文字的歌单
    @GetMapping("/songList/likeTitle/detail")
    public R songListOfLikeTitle(@RequestParam String title) {
        return songListService.likeTitle('%' + title + '%');
    }

    // 返回指定类型的歌单
    @GetMapping("/songList/style/detail")
    public R songListOfStyle(@RequestParam String style) {
        return songListService.likeStyle('%' + style + '%');
    }

    // 更新歌单信息
    @PostMapping("/songList/update")
    public R updateSongListMsg(HttpServletRequest req) {
        String id = req.getParameter("id").trim();
        String title = req.getParameter("title").trim();
        String introduction = req.getParameter("introduction").trim();
        String style = req.getParameter("style").trim();

        SongList songList = new SongList();
        songList.setId(Integer.parseInt(id));
        songList.setTitle(title);
        songList.setIntroduction(introduction);
        songList.setStyle(style);

        boolean res = songListService.updateSongListMsg(songList);
        if (res) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    // 更新歌单图片
    @PostMapping("/songList/img/update")
    public R updateSongListPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        return songListService.updateSongListImg(avatorFile,id);
    }
}
