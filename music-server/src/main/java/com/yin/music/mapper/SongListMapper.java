package com.yin.music.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.music.model.domain.SongList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongListMapper extends BaseMapper<SongList> {

    List<SongList> selectRecommendedSongLists(@Param("limit") Integer limit);

    List<SongList> selectByStyleId(@Param("styleId") Integer styleId);

    Page<SongList> selectPageByStyleId(Page<SongList> page, @Param("styleId") Integer styleId);
}
