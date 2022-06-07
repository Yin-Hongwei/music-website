package com.example.yin.model.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class RankList implements Serializable {

    private Long id;

    private Long songListId;

    private Long consumerId;

    private Integer score;

}
