package com.example.yin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@TableName(value = "`user`")
@Data
public class AppUser {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private Byte sex;

    private String phoneNum;

    private String email;

    private Date birth;

    private String introduction;

    private String location;

    private String avatar;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
