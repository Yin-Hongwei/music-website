package com.yin.music.model.response;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import com.yin.music.model.domain.Banner;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BannerResponse {

    private Integer id;
    private String pic;
    private String url;
    private String title;

    public static BannerResponse from(Banner source) {
        if (source == null) {
            return null;
        }
        BannerResponse target = new BannerResponse();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static List<BannerResponse> fromList(List<Banner> sources) {
        if (sources == null || sources.isEmpty()) {
            return Collections.emptyList();
        }
        return sources.stream().map(BannerResponse::from).collect(Collectors.toList());
    }
}
