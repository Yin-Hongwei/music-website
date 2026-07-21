package com.yin.music.model.response;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import com.yin.music.model.domain.SongList;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SongListResponse {

    private Integer id;
    private String title;
    private String pic;
    /** 风格展示缓存（`-` 拼接），便于列表直接展示 */
    private String style;
    /** 风格详情（id + name） */
    private List<StyleOptionResponse> styles;
    private String introduction;

    public static SongListResponse from(SongList source) {
        if (source == null) {
            return null;
        }
        SongListResponse target = new SongListResponse();
        BeanUtils.copyProperties(source, target);
        target.setStyles(Collections.emptyList());
        return target;
    }

    public static SongListResponse from(SongList source, List<StyleOptionResponse> styles) {
        SongListResponse target = from(source);
        if (target == null) {
            return null;
        }
        List<StyleOptionResponse> safeStyles = styles == null ? Collections.emptyList() : styles;
        target.setStyles(safeStyles);
        if ((target.getStyle() == null || target.getStyle().trim().isEmpty()) && !safeStyles.isEmpty()) {
            target.setStyle(safeStyles.stream().map(StyleOptionResponse::getName).collect(Collectors.joining("-")));
        }
        return target;
    }

    public static List<SongListResponse> fromList(List<SongList> sources) {
        if (sources == null || sources.isEmpty()) {
            return Collections.emptyList();
        }
        return sources.stream().map(SongListResponse::from).collect(Collectors.toList());
    }
}
