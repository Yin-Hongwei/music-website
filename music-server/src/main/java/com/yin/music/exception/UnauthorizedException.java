package com.yin.music.exception;

/**
 * Thrown when a mutating API is called without a valid app-user session.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
