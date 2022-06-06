package com.example.yin.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@Data
public class Collect {
    private Integer id;

    private Integer userId;

    private Byte type;

    private Integer songId;

    private Integer songListId;

    private Date createTime;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
