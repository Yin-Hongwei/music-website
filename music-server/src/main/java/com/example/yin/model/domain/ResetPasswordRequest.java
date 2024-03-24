package com.example.yin.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResetPasswordRequest {
    private String email;
    private String code;
    private String password;
    private String confirmPassword;
}
