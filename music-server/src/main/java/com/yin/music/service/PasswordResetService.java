package com.yin.music.service;

import com.yin.music.model.R;
import com.yin.music.model.request.PasswordResetRequest;

public interface PasswordResetService {

    R<?> sendVerificationCode(String email);

    R<?> resetPassword(PasswordResetRequest request);
}
