package com.yin.music.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectorsResponse {

    private int total;
    private List<AppUserResponse> users;

    public static CollectorsResponse of(List<AppUserResponse> users) {
        List<AppUserResponse> safeUsers = users == null ? Collections.emptyList() : users;
        return new CollectorsResponse(safeUsers.size(), safeUsers);
    }
}
