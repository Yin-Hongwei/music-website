package com.example.yin.controller;

import com.example.yin.common.Message;
import com.example.yin.domain.Collect;
import com.example.yin.service.impl.CollectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class CollectController {

    @Autowired
    private CollectServiceImpl collectService;

    private Message message = new Message();

    // 添加收藏的歌曲
    @PostMapping("/collection/add")
    public Object addCollection(HttpServletRequest req) {
        String userId = req.getParameter("userId");
        String type = req.getParameter("type");
        String songId = req.getParameter("songId");
        String songListId = req.getParameter("songListId");

        Collect collect = new Collect();
        collect.setUserId(Integer.parseInt(userId));
        collect.setType(new Byte(type));
        if (new Byte(type) == 0) {
            collect.setSongId(Integer.parseInt(songId));
        } else if (new Byte(type) == 1) {
            collect.setSongListId(Integer.parseInt(songListId));
        }
        collect.setCreateTime(new Date());

        boolean res = collectService.addCollection(collect);
        if (res) {
            return message.success("收藏成功", true);
        } else {
            return message.error("收藏失败");
        }
    }

    //TODO  这些其实有点偏简单的逻辑  所以就一点 所以放在外面
    // 取消收藏的歌曲
    @DeleteMapping("/collection/delete")
    public Object deleteCollection(HttpServletRequest req) {
        String userId = req.getParameter("userId").trim();
        String songId = req.getParameter("songId").trim();

        boolean res = collectService.deleteCollect(Integer.parseInt(userId), Integer.parseInt(songId));
        if (res) {
            return message.success("取消收藏", false);
        } else {
            return message.error("取消收藏失败");
        }
    }

    // 是否收藏歌曲
    @PostMapping("/collection/status")
    public Object isCollection(HttpServletRequest req) {
        String userId = req.getParameter("userId").trim();
        String songId = req.getParameter("songId").trim();

        boolean res = collectService.existSongId(Integer.parseInt(userId), Integer.parseInt(songId));
        if (res) {
            return message.success("已收藏", true);
        } else {
            return message.success("未收藏", false);
        }
    }

    // 返回的指定用户 ID 收藏的列表
    @GetMapping("/collection/detail")
    public Object collectionOfUser(HttpServletRequest req) {
        String userId = req.getParameter("userId");

        return message.success("取消收藏", collectService.collectionOfUser(Integer.parseInt(userId)));
    }
}
