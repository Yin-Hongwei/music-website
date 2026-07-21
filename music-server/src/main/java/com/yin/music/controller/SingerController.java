package com.yin.music.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.yin.music.model.R;
import com.yin.music.model.request.SingerRequest;
import com.yin.music.service.SingerService;

@RequiredArgsConstructor
@RestController
public class SingerController {

    private final SingerService singerService;


    // 添加歌手
    @PostMapping("/singer/add")
    public R<?> addSinger(@RequestBody SingerRequest addSingerRequest) {
        return singerService.addSinger(addSingerRequest);
    }

    // 删除歌手
    @GetMapping("/singer/delete")
    public R<?> deleteSinger(@RequestParam int id) {
        return singerService.deleteSinger(id);
    }

    // 返回所有歌手
    @GetMapping("/singer")
    public R<?> allSinger() {
        return singerService.allSinger();
    }

    @GetMapping("/singer/page")
    public R<?> pageSinger(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        return singerService.pageSinger(safePage, safeSize);
    }

    // 根据歌手 id 查询详情
    @GetMapping("/singer/detail")
    public R<?> singerOfId(@RequestParam int id) {
        return singerService.singerOfId(id);
    }

    // 根据歌手名查找歌手
    @GetMapping("/singer/name/detail")
    public R<?> singerOfName(@RequestParam String name) {
        return singerService.singerOfName(name);
    }

    // 按歌手分类筛选（singer_style.type：0女 1男 2组合）
    @GetMapping("/singer/type/detail")
    public R<?> singerOfType(@RequestParam int type) {
        return singerService.singerOfType(type);
    }

    // 返回歌手分类
    @GetMapping("/singer/style/list")
    public R<?> singerStyleList() {
        return singerService.allSingerStyle();
    }

    // 更新歌手信息
    @PostMapping("/singer/update")
    public R<?> updateSingerMsg(@RequestBody SingerRequest updateSingerRequest) {
        return singerService.updateSingerMsg(updateSingerRequest);
    }

    // 更新歌手头像
    @PostMapping("/singer/avatar/update")
    public R<?> updateSingerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        return singerService.updateSingerPic(avatorFile, id);
    }
}
