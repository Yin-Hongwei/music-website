package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.domain.ListSong;
import com.example.yin.service.ListSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ListSongController {

    @Autowired
    private ListSongService listSongService;


    // 给歌单添加歌曲
    @PostMapping("/listSong/add")
    public R addListSong(HttpServletRequest req) {
        String songId = req.getParameter("songId").trim();
        String songListId = req.getParameter("songListId").trim();

        ListSong listsong = new ListSong();
        listsong.setSongId(Integer.parseInt(songId));
        listsong.setSongListId(Integer.parseInt(songListId));

        boolean res = listSongService.addListSong(listsong);
        if (res) {
            return R.success("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    // 删除歌单里的歌曲
    @GetMapping("/listSong/delete")
    public R deleteListSong(HttpServletRequest req) {
        String songId = req.getParameter("songId");

        boolean res = listSongService.deleteListSong(Integer.parseInt(songId));
        if (res) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    // 返回歌单里指定歌单 ID 的歌曲
    @GetMapping("/listSong/detail")
    public R listSongOfSongId(@RequestParam int songListId ) {
        return listSongService.listSongOfSongId(songListId);
    }

    // 更新歌单里面的歌曲信息
    @PostMapping("/listSong/update")
    public R updateListSongMsg(HttpServletRequest req) {
        String id = req.getParameter("id").trim();
        String songId = req.getParameter("songId").trim();
        String songListId = req.getParameter("songListId").trim();

        ListSong listsong = new ListSong();
        listsong.setId(Integer.parseInt(id));
        listsong.setSongId(Integer.parseInt(songId));
        listsong.setSongListId(Integer.parseInt(songListId));

        boolean res = listSongService.updateListSongMsg(listsong);
        if (res) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }
}
