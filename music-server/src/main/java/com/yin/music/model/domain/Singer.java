package com.yin.music.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@TableName(value = "singer")
@Data
public class Singer {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    /** 性别：0女 1男 2保密；仅 kind=个人 时有意义 */
    private Byte sex;

    /** 类型：0个人 1组合 2其他 */
    private Byte kind;

    private String pic;

    private Date birth; // 仅 kind=个人 时有意义


    private String location;

    private String introduction;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
