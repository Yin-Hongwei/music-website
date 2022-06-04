package com.example.yin.controller;

import com.example.yin.common.Message;
import com.example.yin.domain.RankList;
import com.example.yin.service.impl.RankListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RankListController {

    @Autowired
    private RankListServiceImpl rankListService;

    private Message message = new Message();

    // 提交评分
    @PostMapping("/rankList/add")
    public Object addRank(HttpServletRequest req) {
        String songListId = req.getParameter("songListId").trim();
        String consumerId = req.getParameter("consumerId").trim();
        String score = req.getParameter("score").trim();

        RankList rankList = new RankList();
        rankList.setSongListId(Long.parseLong(songListId));
        rankList.setConsumerId(Long.parseLong(consumerId));
        rankList.setScore(Integer.parseInt(score));

        boolean res = rankListService.addRank(rankList);
        if (res) {
            return message.success("评价成功");
        } else {
            return message.error("评价失败");
        }
    }

    // 获取指定歌单的评分
    @GetMapping("/rankList")
    public Object rankOfSongListId(HttpServletRequest req) {
        String songListId = req.getParameter("songListId");

        return message.success(null, rankListService.rankOfSongListId(Long.parseLong(songListId)));
    }

    // 获取指定用户的歌单评分
    @GetMapping("/rankList/user")
    public Object getUserRank(HttpServletRequest req) {
        String consumerId = req.getParameter("consumerId");
        String songListId = req.getParameter("songListId");

        return message.success(null, rankListService.getUserRank(Long.parseLong(consumerId), Long.parseLong(songListId)));
    }
}
