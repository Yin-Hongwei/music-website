package com.yin.music.model.response;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import com.yin.music.model.domain.Song;

import java.util.Date;

/**
 * Playlist song detail: song fields plus resolved singerName / songName.
 */
@Data
public class SongDetailResponse {

    private Integer id;
    private Integer singerId;
    /** Pure song title (same as songName). */
    private String name;
    private String songName;
    private String singerName;
    private String introduction;
    private Date createTime;
    private Date updateTime;
    private String pic;
    private String lyric;
    private String url;
    private Integer duration;

    public static SongDetailResponse from(Song source) {
        return from(source, null);
    }

    public static SongDetailResponse from(Song source, String singerName) {
        if (source == null) {
            return null;
        }
        SongDetailResponse target = new SongDetailResponse();
        BeanUtils.copyProperties(source, target);
        String resolvedSinger = singerName != null ? singerName : "";
        String title = SongResponse.stripLegacySingerPrefix(source.getName(), resolvedSinger);
        target.setName(title);
        target.setSongName(title);
        target.setSingerName(resolvedSinger);
        return target;
    }
}
