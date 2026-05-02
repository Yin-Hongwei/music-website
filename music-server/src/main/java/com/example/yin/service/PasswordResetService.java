package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.model.request.PasswordResetRequest;

public interface PasswordResetService {

    R sendVerificationCode(String email);

    R resetPassword(PasswordResetRequest request);
}
