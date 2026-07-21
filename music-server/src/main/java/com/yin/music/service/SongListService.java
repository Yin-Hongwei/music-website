package com.yin.music.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yin.music.model.R;
import com.yin.music.model.domain.SongList;
import com.yin.music.model.request.SongListRequest;
import org.springframework.web.multipart.MultipartFile;

public interface SongListService extends IService<SongList> {

    R<?> addSongList(SongListRequest addSongListRequest);

    R<?> updateSongListMsg(SongListRequest updateSongListRequest);

    R<?> updateSongListImg(MultipartFile avatorFile, int id);

    R<?> deleteSongList(Integer id);

    R<?> allSongList();

    R<?> pageSongList(Integer page, Integer size);

    R<?> songListOfId(Integer id);

    R<?> likeTitle(String title);

    R<?> listByStyleId(Integer styleId);

    R<?> pageByStyleId(Integer styleId, Integer page, Integer size);

    R<?> allSongListStyle();

    R<?> recommendSongList(Integer limit);
}
