package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.model.domain.SongList;
import org.springframework.web.multipart.MultipartFile;

public interface SongListService {

    boolean addSongList (SongList songList);

    boolean updateSongListMsg(SongList songList);

    R updateSongListImg(MultipartFile avatorFile, int id);

    boolean deleteSongList(Integer id);

    R allSongList();

    R likeTitle(String title);

    R likeStyle(String style);
}
