package com.example.yin.model.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class SongList {

    private Integer id;

    private String title;

    private String pic;

    private String style;

    private String introduction;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
