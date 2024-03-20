package com.example.yin.controller;

import com.alibaba.excel.EasyExcel;
import com.example.yin.common.R;
import com.example.yin.model.domain.SongList;
import com.example.yin.model.request.ListSongRequest;
import com.example.yin.service.ListSongService;
import com.example.yin.service.SongListService;
import com.example.yin.utils.TestFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
public class ListSongController {

    @Autowired
    private ListSongService listSongService;
    @Autowired
    private SongListService service;
    // 给歌单添加歌曲
    @PostMapping("/listSong/add")
    public R addListSong(@RequestBody ListSongRequest addListSongRequest) {
        return listSongService.addListSong(addListSongRequest);
    }

    // 删除歌单里的歌曲
    @GetMapping("/listSong/delete")
    public R deleteListSong(@RequestParam int songId) {
        return listSongService.deleteListSong(songId);
    }

    // 返回歌单里指定歌单 ID 的歌曲
    @GetMapping("/listSong/detail")
    public R listSongOfSongId(@RequestParam int songListId) {
        return listSongService.listSongOfSongId(songListId);
    }

    // 更新歌单里面的歌曲信息
    @PostMapping("/listSong/update")
    public R updateListSongMsg(@RequestBody ListSongRequest updateListSongRequest) {
        return listSongService.updateListSongMsg(updateListSongRequest);
    }
    //导出歌单
    @GetMapping("/excle")
    public ResponseEntity<Resource> getExcle(HttpServletRequest request) throws IOException {
        String fileName = "SongList" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, SongList.class).sheet("模板").doWrite(data());
        File file = new File(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        byte[] content = Files.readAllBytes(file.toPath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(content.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    private List<SongList> data() {
        List<SongList> allSong = service.findAllSong();
        return allSong;
    }

}
