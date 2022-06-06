package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.request.SongListRequest;
import org.springframework.web.multipart.MultipartFile;

public interface SongListService {

    R addSongList (SongListRequest addSongListRequest);

    R updateSongListMsg(SongListRequest updateSongListRequest);

    R updateSongListImg(MultipartFile avatorFile, int id);

    R deleteSongList(Integer id);

    R allSongList();

    R likeTitle(String title);

    R likeStyle(String style);
}
