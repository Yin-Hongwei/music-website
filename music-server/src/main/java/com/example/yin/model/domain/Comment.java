package com.example.yin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@TableName(value = "comment")
@Data
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer songId;

    @TableField("song_sheet_id")
    private Integer songListId;

    private String content;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Byte type;

    @TableField("like_count")
    private Integer likeCount;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
