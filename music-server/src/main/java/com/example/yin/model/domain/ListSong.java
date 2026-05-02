package com.example.yin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@TableName(value = "song_sheet_song")
@Data
public class ListSong implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer songId;

    @TableField("song_sheet_id")
    private Integer songListId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
