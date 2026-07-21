package com.yin.music.model.response;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PageResponse<T> {

    private List<T> records;
    private long total;
    private int page;
    private int size;
    private boolean hasMore;

    public static <T> PageResponse<T> of(List<T> records, long total, int page, int size) {
        PageResponse<T> data = new PageResponse<>();
        data.setRecords(records == null ? Collections.emptyList() : records);
        data.setTotal(total);
        data.setPage(page);
        data.setSize(size);
        data.setHasMore((long) page * size < total);
        return data;
    }
}
