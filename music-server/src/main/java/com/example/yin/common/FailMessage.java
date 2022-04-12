package com.example.yin.common;

import com.alibaba.fastjson.JSONObject;

public class FailMessage {
    JSONObject jsonObject = new JSONObject();

    public FailMessage(String message) {
        jsonObject.put("code", 200);
        jsonObject.put("message", message);
        jsonObject.put("success", false);
        jsonObject.put("type", "error");
        jsonObject.put("data", null);
    }

    public JSONObject getMessage() {
        return jsonObject;
    }
}