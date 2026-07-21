package com.yin.music.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCollectResponse {

    private List<SongResponse> songs;
    private List<SongListResponse> songSheets;

    public static UserCollectResponse of(List<SongResponse> songs, List<SongListResponse> songSheets) {
        return new UserCollectResponse(
                songs == null ? Collections.emptyList() : songs,
                songSheets == null ? Collections.emptyList() : songSheets
        );
    }
}
