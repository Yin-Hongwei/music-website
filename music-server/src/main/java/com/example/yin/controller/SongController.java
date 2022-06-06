package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.domain.Song;
import com.example.yin.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@RestController
public class SongController {

    @Autowired
    private SongService songService;


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 文件最大10M,DataUnit提供5中类型B,KB,MB,GB,TB
        factory.setMaxFileSize(DataSize.of(20, DataUnit.MEGABYTES));
        // 设置总上传数据总大小10M
        factory.setMaxRequestSize(DataSize.of(20, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }


    // 添加歌曲
    @PostMapping("/song/add")
    public R addSong(HttpServletRequest req, @RequestParam("file") MultipartFile mpfile) {
        String singer_id = req.getParameter("singerId").trim();
        String name = req.getParameter("name").trim();
        String introduction = req.getParameter("introduction").trim();
        String pic = "/img/songPic/tubiao.jpg";
        String lyric = req.getParameter("lyric").trim();

        String fileName = mpfile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/song/" + fileName;
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
                return R.success("上传成功", storeUrlPath);
            } else {
                return R.error("上传失败");
            }
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
    }

    // 删除歌曲
    @DeleteMapping("/song/delete")
    public R deleteSong(HttpServletRequest req) {
        String id = req.getParameter("id");

        boolean res = songService.deleteSong(Integer.parseInt(id));
        if (res) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    // 返回所有歌曲
    @GetMapping("/song")
    public R allSong() {
        return R.success(null, songService.allSong());
    }

    //TODO ok
    // 返回指定歌曲ID的歌曲
    @GetMapping("/song/detail")
    public R songOfId(HttpServletRequest req) {
        String id = req.getParameter("id");

        return R.success(null, songService.songOfId(Integer.parseInt(id)));
    }

    // 返回指定歌手ID的歌曲
    @GetMapping("/song/singer/detail")
    public R songOfSingerId(HttpServletRequest req) {
        String singerId = req.getParameter("singerId");

        return R.success(null, songService.songOfSingerId(Integer.parseInt(singerId)));
    }

    // 返回指定歌手名的歌曲
    @GetMapping("/song/singerName/detail")
    public R songOfSingerName(HttpServletRequest req) {
        String name = req.getParameter("name");

        return R.success(null, songService.songOfSingerName('%' + name + '%'));
    }

    // 更新歌曲信息
    @PostMapping("/song/update")
    public R updateSongMsg(HttpServletRequest req) {
        String id = req.getParameter("id").trim();
        String singerId = req.getParameter("singerId").trim();
        String name = req.getParameter("name").trim();
        String introduction = req.getParameter("introduction").trim();
        String lyric = req.getParameter("lyric").trim();

        Song song = new Song();
        song.setId(Integer.parseInt(id));
        song.setSingerId(Integer.parseInt(singerId));
        song.setName(name);
        song.setIntroduction(introduction);
        song.setUpdateTime(new Date());
        song.setLyric(lyric);

        boolean res = songService.updateSongMsg(song);
        if (res) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    // 更新歌曲图片
    @PostMapping("/song/img/update")
    public R updateSongPic(@RequestParam("file") MultipartFile urlFile, @RequestParam("id") int id) {
        String fileName = System.currentTimeMillis() + urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "songPic";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/img/songPic/" + fileName;
        try {
            urlFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setPic(storeUrlPath);
            boolean res = songService.updateSongPic(song);
            if (res) {
                return R.success("上传成功", storeUrlPath);
            } else {
                return R.error("上传失败");
            }
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
    }

    // 更新歌曲
    @PostMapping("/song/url/update")
    public R updateSongUrl(@RequestParam("file") MultipartFile urlFile, @RequestParam("id") int id) {
        String fileName = urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/song/" + fileName;
        try {
            urlFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setUrl(storeUrlPath);
            boolean res = songService.updateSongUrl(song);
            if (res) {
                return R.success("更新成功", storeUrlPath);
            } else {
                return R.error("更新失败");
            }
        } catch (IOException e) {
            return R.fatal("更新失败" + e.getMessage());
        }
    }
}
