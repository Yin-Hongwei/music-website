package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.domain.SongList;
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

    // 返回所有歌单
    @GetMapping("/songList")
    public R allSongList() {
        return R.success(null, songListService.allSongList());
    }

    // 返回标题包含文字的歌单
    @GetMapping("/songList/likeTitle/detail")
    public R songListOfLikeTitle(HttpServletRequest req) {
        String title = req.getParameter("title").trim();

        return R.success(null, songListService.likeTitle('%' + title + '%'));
    }

    // 返回指定类型的歌单
    @GetMapping("/songList/style/detail")
    public R songListOfStyle(HttpServletRequest req) {
        String style = req.getParameter("style").trim();

        return R.success(null, songListService.likeStyle('%' + style + '%'));
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
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "songListPic";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String imgPath = "/img/songListPic/" + fileName;
        try {
            avatorFile.transferTo(dest);
            SongList songList = new SongList();
            songList.setId(id);
            songList.setPic(imgPath);

            boolean res = songListService.updateSongListImg(songList);
            if (res) {
                return R.success("上传成功", imgPath);
            } else {
                return R.error("上传失败");
            }
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
    }
}
