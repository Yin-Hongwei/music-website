package com.yin.music.model;

import lombok.Data;

/**
 * 统一 API 响应壳（协议层，非业务 DTO）。
 * 业务载荷放在 {@code model.response}；本类只承载线格式
 * {@code code / message / success / data}（{@code type} 为历史兼容字段）。
 *
 * @param <T> {@code data} 载荷类型
 */
@Data
public class R<T> {

    public static final int CODE_OK = 200;
    public static final int CODE_FATAL = 500;

    private int code;
    private String message;
    private Boolean success;
    private T data;
    /** 历史字段：success | warning | error，对齐 Element Plus Message type */
    private String type;

    private R() {
    }

    public static <T> R<T> success(String message) {
        return success(message, null);
    }

    public static <T> R<T> success(String message, T data) {
        return of(CODE_OK, message, true, "success", data);
    }

    public static <T> R<T> warning(String message) {
        // 业务软失败：保持 code=200，由 success/type 区分（兼容现有前端）
        return of(CODE_OK, message, false, "warning", null);
    }

    public static <T> R<T> error(String message) {
        // 业务失败：保持 code=200，由 success/type 区分（兼容现有前端）
        return of(CODE_OK, message, false, "error", null);
    }

    public static <T> R<T> fatal(String message) {
        return of(CODE_FATAL, message, false, "error", null);
    }

    private static <T> R<T> of(int code, String message, boolean success, String type, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        r.setSuccess(success);
        r.setType(type);
        r.setData(data);
        return r;
    }
}
