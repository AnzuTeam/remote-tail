package com.prhythm.core.generic.exception;

/**
 * 轉換 {@link Exception} 為 {@link RuntimeException}
 * Created by nanashi07 on 15/11/26.
 */
public final class RecessiveException extends RuntimeException {
    public RecessiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
