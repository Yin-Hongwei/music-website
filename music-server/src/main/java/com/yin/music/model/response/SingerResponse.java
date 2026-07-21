package com.yin.music.model.response;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import com.yin.music.model.domain.Singer;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SingerResponse {

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

    public static SingerResponse from(Singer source) {
        if (source == null) {
            return null;
        }
        SingerResponse target = new SingerResponse();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static List<SingerResponse> fromList(List<Singer> sources) {
        if (sources == null || sources.isEmpty()) {
            return Collections.emptyList();
        }
        return sources.stream().map(SingerResponse::from).collect(Collectors.toList());
    }
}
