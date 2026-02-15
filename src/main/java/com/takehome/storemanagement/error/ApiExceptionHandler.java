package com.takehome.storemanagement.error;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler({
            ProductNotFoundException.class,
            SkuAlreadyExistsException.class,
            MethodArgumentNotValidException.class,
            Exception.class
    })
    public ResponseEntity<ApiError> handle(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();

        return switch (ex) {
            case ProductNotFoundException e -> {
                log.warn("Not found: {}", e.getMessage());
                yield ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiError.of(404, "PRODUCT_NOT_FOUND", "Product not found", path));
            }
            case SkuAlreadyExistsException e -> {
                log.warn("Conflict: {}", e.getMessage());
                yield ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiError.of(409, "SKU_ALREADY_EXISTS", "SKU already exists", path));
            }
            case MethodArgumentNotValidException e -> {
                List<String> details = e.getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(error -> {
                            if (error instanceof FieldError fe) {
                                return fe.getField() + ": " + fe.getDefaultMessage();
                            }
                            return error.getDefaultMessage();
                        })
                        .toList();

                yield ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiError.of(400, "VALIDATION_ERROR", "Invalid request", path, details));
            }
            default -> {
                log.error("Unexpected error on path={}", path, ex);
                yield ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiError.of(500, "INTERNAL_ERROR", "Unexpected error", path));
            }
        };
    }
}
