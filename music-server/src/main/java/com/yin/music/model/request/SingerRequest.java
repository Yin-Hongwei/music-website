package com.yin.music.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class SingerRequest {
    private Integer id;

    private String name;

    /** 性别：0女 1男 2保密；仅个人有效 */
    private Byte sex;

    /** 类型：0个人 1组合 2其他 */
    private Byte kind;

    private String pic;

    private Date birth;

    private String location;

    private String introduction;
}
