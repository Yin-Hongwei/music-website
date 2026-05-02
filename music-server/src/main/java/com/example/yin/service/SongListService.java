package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.common.R;
import com.example.yin.model.domain.SongList;
import com.example.yin.model.request.SongListRequest;
import org.springframework.web.multipart.MultipartFile;

public interface SongListService extends IService<SongList> {

    R addSongList(SongListRequest addSongListRequest);

    R updateSongListMsg(SongListRequest updateSongListRequest);

    R updateSongListImg(MultipartFile avatorFile, int id);

    R deleteSongList(Integer id);

    R allSongList();

    R pageSongList(Integer page, Integer size);

    R songListOfId(Integer id);

    R likeTitle(String title);

    R likeStyle(String style);

    R pageLikeStyle(String style, Integer page, Integer size);

    R allSongListStyle();

    R recommendSongList(Integer limit);
}
