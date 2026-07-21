package com.yin.music.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StyleOptionResponse {

    private Integer id;
    private String name;
    private Integer type;

    public static StyleOptionResponse of(Integer id, String name) {
        return new StyleOptionResponse(id, name, null);
    }

    public static StyleOptionResponse of(Integer id, String name, Integer type) {
        return new StyleOptionResponse(id, name, type);
    }
}
