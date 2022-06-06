package com.example.yin.model.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@Data
public class ListSong implements Serializable {

    private Integer id;

    private Integer songId;

    private Integer songListId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
