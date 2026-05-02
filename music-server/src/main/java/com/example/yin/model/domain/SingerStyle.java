package com.example.yin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "singer_style")
@Data
public class SingerStyle {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer type;
}
