package com.yin.music.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {

    private String url;

    public static UploadResponse of(String url) {
        return new UploadResponse(url);
    }
}
