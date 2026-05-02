package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.request.SingerRequest;
import com.example.yin.service.SingerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class SingerController {

    private final SingerService singerService;


    // 添加歌手
    @PostMapping("/singer/add")
    public R addSinger(@RequestBody SingerRequest addSingerRequest) {
        return singerService.addSinger(addSingerRequest);
    }

    // 删除歌手
    @GetMapping("/singer/delete")
    public R deleteSinger(@RequestParam int id) {
        return singerService.deleteSinger(id);
    }

    // 返回所有歌手
    @GetMapping("/singer")
    public R allSinger() {
        return singerService.allSinger();
    }

    @GetMapping("/singer/page")
    public R pageSinger(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        return singerService.pageSinger(safePage, safeSize);
    }

    // 根据歌手名查找歌手
    @GetMapping("/singer/name/detail")
    public R singerOfName(@RequestParam String name) {
        return singerService.singerOfName(name);
    }

    // 根据歌手性别查找歌手
    @GetMapping("/singer/sex/detail")
    public R singerOfSex(@RequestParam int sex) {
        return singerService.singerOfSex(sex);
    }

    // 返回歌手分类
    @GetMapping("/singer/style/list")
    public R singerStyleList() {
        return singerService.allSingerStyle();
    }

    // 更新歌手信息
    @PostMapping("/singer/update")
    public R updateSingerMsg(@RequestBody SingerRequest updateSingerRequest) {
        return singerService.updateSingerMsg(updateSingerRequest);
    }

    // 更新歌手头像
    @PostMapping("/singer/avatar/update")
    public R updateSingerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        return singerService.updateSingerPic(avatorFile, id);
    }
}
