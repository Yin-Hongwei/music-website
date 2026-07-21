package com.yin.music.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.music.model.R;
import com.yin.music.model.domain.Song;
import com.yin.music.model.request.SongRequest;

import org.springframework.web.multipart.MultipartFile;

public interface SongService extends IService<Song> {

    R<?> addSong (SongRequest addSongRequest,  MultipartFile mpfile);

    R<?> updateSongMsg(SongRequest updateSongRequest);

    R<?> updateSongUrl(MultipartFile urlFile, int id, Integer duration);

    R<?> updateSongPic(MultipartFile urlFile, int id);

    R<?> deleteSong(Integer id);

    R<?> allSong();

    R<?> pageSong(Integer page, Integer size);

    R<?> songOfSingerId(Integer singerId);

    R<?> songOfId(Integer id);

    R<?> songOfSingerName(String name);
}
