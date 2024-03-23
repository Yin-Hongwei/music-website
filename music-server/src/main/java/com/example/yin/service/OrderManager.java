package com.example.yin.service;

import com.example.yin.model.domain.Order;
public interface OrderManager {
    void send(Order order,String reciveAddress);
}