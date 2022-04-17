package com.example.yin.common;

import com.alibaba.fastjson.JSONObject;

public class SuccessMessage<T> {
    JSONObject jsonObject = new JSONObject();

    public SuccessMessage(String message) {
        jsonObject.put("code", 200);
        jsonObject.put("message", message);
        jsonObject.put("success", true);
        jsonObject.put("type", "success");
        jsonObject.put("data", null);
    }

    public SuccessMessage(String message, T data) {
        jsonObject.put("code", 200);
        jsonObject.put("message", message);
        jsonObject.put("success", true);
        jsonObject.put("type", "success");
        jsonObject.put("data", data);
    }

    public JSONObject getMessage() {
        return jsonObject;
    }
}
