package com.example.yin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "song_sheet_style")
@Data
public class SongListStyle {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;
}
