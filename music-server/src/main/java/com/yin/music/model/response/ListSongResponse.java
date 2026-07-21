package com.yin.music.model.response;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import com.yin.music.model.domain.ListSong;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ListSongResponse {

    private Integer id;
    private Integer songId;
    private Integer songListId;

    public static ListSongResponse from(ListSong source) {
        if (source == null) {
            return null;
        }
        ListSongResponse target = new ListSongResponse();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static List<ListSongResponse> fromList(List<ListSong> sources) {
        if (sources == null || sources.isEmpty()) {
            return Collections.emptyList();
        }
        return sources.stream().map(ListSongResponse::from).collect(Collectors.toList());
    }
}
