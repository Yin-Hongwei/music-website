package com.yin.music.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "song_sheet_style_rel")
@Data
public class SongListStyleRel {

    @TableField("song_sheet_id")
    private Integer songListId;

    @TableField("style_id")
    private Integer styleId;
}
