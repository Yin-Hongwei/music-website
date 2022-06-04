package com.example.yin.common;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/4 19:04
 **/
public class Message<T> {
    public Message() {
    }

    /**
     * 错误
     */
    public JSONObject error(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("message", message);
        jsonObject.put("success", false);
        jsonObject.put("type", "error");
        jsonObject.put("data", null);
        return jsonObject;
    }

    public JSONObject fatal(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 500);
        jsonObject.put("message", message);
        jsonObject.put("success", false);
        jsonObject.put("type", "error");
        jsonObject.put("data", null);
        return jsonObject;
    }

    public JSONObject success(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("message", message);
        jsonObject.put("success", true);
        jsonObject.put("type", "success");
        jsonObject.put("data", null);
        return jsonObject;
    }

    public JSONObject success(String message, T data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("message", message);
        jsonObject.put("success", true);
        jsonObject.put("type", "success");
        jsonObject.put("data", data);
        return jsonObject;
    }

    public JSONObject warning(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("message", message);
        jsonObject.put("success", false);
        jsonObject.put("type", "warning");
        jsonObject.put("data", null);
        return jsonObject;
    }
}
