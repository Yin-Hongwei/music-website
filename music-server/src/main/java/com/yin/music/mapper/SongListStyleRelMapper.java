package com.yin.music.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yin.music.model.domain.SongListStyleRel;
import com.yin.music.model.response.SongListStyleLink;
import com.yin.music.model.response.StyleOptionResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SongListStyleRelMapper extends BaseMapper<SongListStyleRel> {

    List<StyleOptionResponse> selectStylesBySongListId(@Param("songListId") Integer songListId);

    List<SongListStyleLink> selectStylesBySongListIds(@Param("songListIds") Collection<Integer> songListIds);

    int deleteBySongListId(@Param("songListId") Integer songListId);

    int insertBatch(@Param("rows") List<SongListStyleRel> rows);
}
