package com.yin.music.exception;

import com.yin.music.model.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<R<?>> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(R.error(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<R<?>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(R.error(ex.getMessage() != null ? ex.getMessage() : "请求参数不合法"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Request validation failed: {}", ex.getMessage());
        return R.error("请求参数不合法");
    }

    @ExceptionHandler(ClientAbortException.class)
    public void handleClientAbort(ClientAbortException ex) {
        log.debug("Client closed connection before response finished: {}", ex.getMessage());
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<R<?>> handleRedisFailure(RedisConnectionFailureException ex) {
        log.warn("Redis unavailable: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(R.error("缓存服务不可用，请检查 Redis 配置"));
    }

    /**
     * 兜底异常处理：
     * - 客户端断开连接（非错误）：记录日志，不返回响应
     * - 响应已提交：记录警告，不返回响应
     * - 其他异常：记录错误日志，返回 500 错误
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<R<?>> handleGenericException(Exception ex, HttpServletResponse response) {
        if (isBenignClientDisconnect(ex)) {
            log.debug("Client disconnect (benign): {}", ex.getMessage());
            return null;
        }
        if (response.isCommitted()) {
            log.warn("Exception after response committed; skipping JSON error body: {}", ex.toString());
            return null;
        }
        log.error("Unhandled server exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(R.fatal("服务器内部错误，请稍后重试"));
    }

    private static boolean isBenignClientDisconnect(Throwable ex) {
        for (Throwable t = ex; t != null; t = t.getCause()) {
            if (t instanceof ClientAbortException) {
                return true;
            }
            if (t instanceof IOException) {
                String m = t.getMessage();
                if (m != null && (m.contains("Broken pipe") || m.contains("Connection reset"))) {
                    return true;
                }
            }
        }
        return false;
    }
}
