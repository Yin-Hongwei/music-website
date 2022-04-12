package com.example.yin.controller;

import com.example.yin.common.FailMessage;
import com.example.yin.common.SuccessMessage;
import com.example.yin.domain.RankList;
import com.example.yin.service.impl.RankListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RankListController {

    @Autowired
    private RankListServiceImpl rankListService;

    // 提交评分
    @ResponseBody
    @RequestMapping(value = "/rankList/add", method = RequestMethod.POST)
    public Object addRank(HttpServletRequest req) {
        String songListId = req.getParameter("songListId").trim();
        String consumerId = req.getParameter("consumerId").trim();
        String score = req.getParameter("score").trim();

        RankList rank_list = new RankList();
        rank_list.setSongListId(Long.parseLong(songListId));
        rank_list.setConsumerId(Long.parseLong(consumerId));
        rank_list.setScore(Integer.parseInt(score));

        boolean res = rankListService.addRank(rank_list);
        if (res) {
            return new SuccessMessage("评价成功").getMessage();
        } else {
            return new FailMessage("评价失败").getMessage();
        }
    }

    // 获取指定歌单的评分
    @RequestMapping(value = "/rankList", method = RequestMethod.GET)
    public Object rankOfSongListId(HttpServletRequest req) {
        String songListId = req.getParameter("songListId");
        return rankListService.rankOfSongListId(Long.parseLong(songListId));
    }

    // 获取指定用户的歌单评分
    @RequestMapping(value = "/rankList/user", method = RequestMethod.GET)
    public Object getUserRank(HttpServletRequest req) {
        String consumerId = req.getParameter("consumerId");
        String songListId = req.getParameter("songListId");
        return rankListService.getUserRank(Long.parseLong(consumerId), Long.parseLong(songListId));
    }
}
