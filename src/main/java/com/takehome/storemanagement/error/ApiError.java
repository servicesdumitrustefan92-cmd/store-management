package com.takehome.storemanagement.error;

import java.util.List;

public record ApiError(
        int status,
        String code,
        String message,
        String path,
        List<String> details
) {
    public static ApiError of(int status, String code, String message, String path) {
        return new ApiError(status, code, message, path, List.of());
    }

    public static ApiError of(int status, String code, String message, String path, List<String> details) {
        return new ApiError(status, code, message, path, details == null ? List.of() : details);
    }
}
