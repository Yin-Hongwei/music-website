package com.example.yin.service;

import com.example.yin.common.R;
import com.example.yin.domain.Consumer;
import com.example.yin.request.ConsumerRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ConsumerService {

    Object addUser(HttpServletRequest request);

    boolean updateUserMsg(Consumer consumer);

    boolean updateUserAvator(Consumer consumer);

    boolean updatePassword(Consumer consumer);

    boolean existUser(String username);

    boolean verityPasswd(String username, String password);

    boolean deleteUser(Integer id);

    List<Consumer> allUser();

    List<Consumer> userOfId(Integer id);

    R loginStatus(ConsumerRequest loginRequest);

}
