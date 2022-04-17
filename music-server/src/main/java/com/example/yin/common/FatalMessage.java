package com.example.yin.common;

import com.alibaba.fastjson.JSONObject;

public class FatalMessage {
    JSONObject jsonObject = new JSONObject();

    public FatalMessage(String message) {
        jsonObject.put("code", 500);
        jsonObject.put("message", message);
        jsonObject.put("success", false);
        jsonObject.put("type", "error");
        jsonObject.put("data", null);
    }

    public JSONObject getMessage() {
        return jsonObject;
    }
}