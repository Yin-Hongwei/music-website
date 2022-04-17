package com.example.yin.common;

import com.alibaba.fastjson.JSONObject;

public class WarningMessage {
    JSONObject jsonObject = new JSONObject();

    public WarningMessage(String message) {
        jsonObject.put("code", 200);
        jsonObject.put("message", message);
        jsonObject.put("success", false);
        jsonObject.put("type", "warning");
        jsonObject.put("data", null);
    }

    public JSONObject getMessage() {
        return jsonObject;
    }
}
