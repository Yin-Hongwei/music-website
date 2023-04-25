package com.example.yin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@TableName(value = "song_list")
@Data
public class SongList {

    @TableId(type = IdType.AUTO)
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
