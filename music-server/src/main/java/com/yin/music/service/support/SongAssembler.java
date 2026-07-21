package com.yin.music.service.support;

import com.yin.music.mapper.SingerMapper;
import com.yin.music.model.domain.Singer;
import com.yin.music.model.domain.Song;
import com.yin.music.model.response.SongDetailResponse;
import com.yin.music.model.response.SongResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Builds song API payloads with {@code singerName} resolved from {@code singer_id}.
 */
@Component
@RequiredArgsConstructor
public class SongAssembler {

    private final SingerMapper singerMapper;

    public Map<Integer, String> singerNameByIds(Collection<Integer> singerIds) {
        if (singerIds == null || singerIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Integer> ids = singerIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return singerMapper.selectBatchIds(ids).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Singer::getId, Singer::getName, (a, b) -> a));
    }

    public SongResponse toResponse(Song song) {
        if (song == null) {
            return null;
        }
        String singerName = "";
        if (song.getSingerId() != null) {
            Singer singer = singerMapper.selectById(song.getSingerId());
            if (singer != null && singer.getName() != null) {
                singerName = singer.getName();
            }
        }
        return SongResponse.from(song, singerName);
    }

    public List<SongResponse> toResponseList(List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Integer, String> names = singerNameByIds(
                songs.stream().map(Song::getSingerId).collect(Collectors.toList()));
        return songs.stream()
                .map(song -> SongResponse.from(
                        song,
                        names.getOrDefault(song.getSingerId(), "")))
                .collect(Collectors.toList());
    }

    public SongDetailResponse toDetail(Song song) {
        if (song == null) {
            return null;
        }
        Map<Integer, String> names = singerNameByIds(
                song.getSingerId() == null
                        ? Collections.emptyList()
                        : Collections.singletonList(song.getSingerId()));
        return SongDetailResponse.from(song, names.getOrDefault(song.getSingerId(), ""));
    }

    public List<SongDetailResponse> toDetailList(List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Integer, String> names = singerNameByIds(
                songs.stream().map(Song::getSingerId).collect(Collectors.toList()));
        return songs.stream()
                .map(song -> SongDetailResponse.from(
                        song,
                        names.getOrDefault(song.getSingerId(), "")))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
