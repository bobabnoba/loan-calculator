package com.github.bobabnoba.loancalculator.web.error;

import java.time.Instant;

public record ErrorResponse(Instant timestamp, int status, String error, String message, String path, String code) {
    public static ErrorResponse of(int status, String error, String message, String path, String code) {
        return new ErrorResponse(Instant.now(), status, error, message, path, code);
    }
}