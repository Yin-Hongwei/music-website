package com.yin.music.model.response;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import com.yin.music.model.domain.Song;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class SongResponse {

    private Integer id;
    private Integer singerId;
    /** Pure song title (no singer prefix). */
    private String name;
    /** Resolved from singer table via singerId. */
    private String singerName;
    private String introduction;
    private Date createTime;
    private Date updateTime;
    private String pic;
    private String lyric;
    private String url;
    private Integer duration;

    public static SongResponse from(Song source) {
        return from(source, null);
    }

    public static SongResponse from(Song source, String singerName) {
        if (source == null) {
            return null;
        }
        SongResponse target = new SongResponse();
        BeanUtils.copyProperties(source, target);
        String resolvedSinger = singerName != null ? singerName : "";
        target.setSingerName(resolvedSinger);
        target.setName(stripLegacySingerPrefix(source.getName(), resolvedSinger));
        return target;
    }

    /**
     * Legacy rows stored {@code 歌手-歌名} (optional spaces around {@code -});
     * new rows store pure title. Strip the singer prefix when present.
     */
    static String stripLegacySingerPrefix(String name, String singerName) {
        if (name == null) {
            return "";
        }
        String title = name.trim();
        if (singerName == null || singerName.isBlank()) {
            return title;
        }
        String singer = singerName.trim();
        String spacedPrefix = singer + " - ";
        if (title.startsWith(spacedPrefix)) {
            return title.substring(spacedPrefix.length()).trim();
        }
        String prefix = singer + "-";
        if (title.startsWith(prefix)) {
            return title.substring(prefix.length()).trim();
        }
        return title;
    }

    public static List<SongResponse> fromList(List<Song> sources) {
        if (sources == null || sources.isEmpty()) {
            return Collections.emptyList();
        }
        return sources.stream().map(SongResponse::from).collect(Collectors.toList());
    }

    public static List<SongResponse> fromList(List<Song> sources, Map<Integer, String> singerNames) {
        if (sources == null || sources.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Integer, String> names = singerNames != null ? singerNames : Collections.emptyMap();
        return sources.stream()
                .map(song -> from(song, names.getOrDefault(song.getSingerId(), "")))
                .collect(Collectors.toList());
    }
}
