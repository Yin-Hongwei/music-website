package com.yin.music.model.request;

import lombok.Data;

import java.util.List;

@Data
public class SongListRequest {
    private Integer id;

    private String title;

    private String pic;

    /** 风格 id 列表（多对多，源数据） */
    private List<Integer> styleIds;

    private String introduction;
}
