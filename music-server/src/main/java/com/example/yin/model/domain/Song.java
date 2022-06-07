package com.example.yin.model.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@Data
public class Song {

    private Integer id;

    private Integer singerId;

    private String name;

    private String introduction;

    private Date createTime;

    private Date updateTime;

    private String pic;

    private String lyric;

    private String url;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
