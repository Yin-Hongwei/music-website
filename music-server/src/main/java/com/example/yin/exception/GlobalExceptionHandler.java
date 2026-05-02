package com.example.yin.exception;

import com.example.yin.common.R;
import java.io.IOException;
import java.net.ConnectException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Request validation failed: {}", ex.getMessage());
        return R.error("请求参数不合法");
    }

    @ExceptionHandler(ClientAbortException.class)
    public void handleClientAbort(ClientAbortException ex) {
        log.debug("Client closed connection before response finished: {}", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<R> handleGenericException(Exception ex, HttpServletResponse response) {
        if (isBenignClientDisconnect(ex)) {
            log.debug("Client disconnect (benign): {}", ex.getMessage());
            return null;
        }
        if (response.isCommitted()) {
            log.warn("Exception after response committed; skipping JSON error body: {}", ex.toString());
            return null;
        }
        if (findCause(ex, ConnectException.class) != null) {
            log.warn("Upstream connection refused (e.g. MinIO not running): {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(R.error("存储服务不可用，请检查 MinIO 是否已启动"));
        }
        log.error("Unhandled server exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(R.fatal("服务器内部错误，请稍后重试"));
    }

    private static <T extends Throwable> T findCause(Throwable ex, Class<T> type) {
        for (Throwable t = ex; t != null; t = t.getCause()) {
            if (type.isInstance(t)) {
                return type.cast(t);
            }
        }
        return null;
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
