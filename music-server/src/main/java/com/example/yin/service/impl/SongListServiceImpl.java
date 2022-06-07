package com.example.yin.service.impl;

import com.example.yin.common.R;
import com.example.yin.mapper.SongListMapper;
import com.example.yin.model.domain.SongList;
import com.example.yin.service.SongListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class SongListServiceImpl implements SongListService {

    @Autowired
    private SongListMapper songListMapper;

    @Override
    public boolean updateSongListMsg(SongList songList) {
        return songListMapper.updateSongListMsg(songList) > 0 ? true : false;
    }

    @Override
    public boolean deleteSongList(Integer id) {
        return songListMapper.deleteSongList(id) > 0 ? true : false;
    }

    @Override
    public R allSongList() {
        return R.success(null, songListMapper.allSongList());
    }

    @Override
    public R likeTitle(String title) {
        return R.success(null, songListMapper.likeTitle(title));
    }

    @Override
    public R likeStyle(String style) {
        return R.success(null, songListMapper.likeStyle(style));
    }

    @Override
    public boolean addSongList(SongList songList) {
        return songListMapper.insertSelective(songList) > 0 ? true : false;
    }

    @Override
    public R updateSongListImg(MultipartFile avatorFile, @RequestParam("id") int id) {
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
        } catch (IOException e) {
            return R.fatal("上传失败" + e.getMessage());
        }
        SongList songList = new SongList();
        songList.setId(id);
        songList.setPic(imgPath);
        if (songListMapper.updateSongListImg(songList) > 0) {
            return R.success("上传成功", imgPath);
        } else {
            return R.error("上传失败");
        }
    }
}
