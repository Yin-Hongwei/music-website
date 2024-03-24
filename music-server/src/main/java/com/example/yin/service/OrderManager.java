package com.example.yin.service;

import com.example.yin.model.domain.Order;
public interface OrderManager {
    void sendPassword(Order order,String reciveAddress);
    void sendCode(String code,String reciveAddress);
}