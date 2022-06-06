package com.example.yin.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class Admin {
    private Integer id;

    private String name;

    private String password;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
