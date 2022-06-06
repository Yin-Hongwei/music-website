package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.request.SongRequest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface SongService {

    R addSong (HttpServletRequest req, @RequestParam("file") MultipartFile mpfile);

    R updateSongMsg(SongRequest updateSongRequest);

    R updateSongUrl(MultipartFile urlFile, int id);

    R updateSongPic(MultipartFile urlFile, int id);

    R deleteSong(Integer id);

    R allSong();

    R songOfSingerId(Integer singerId);

    R songOfId(Integer id);

    R songOfSingerName(String name);
}
