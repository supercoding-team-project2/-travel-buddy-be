package com.github.travelbuddy.common.response;

import com.github.travelbuddy.common.enums.ErrorType;
import com.github.travelbuddy.common.enums.ResponseType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ApiResponse<T> {
    private ResponseType responseType;
    private String message;
    private HttpStatus status;
    private T data;

    public static ApiResponse<Object> success() {
        return ApiResponse.builder().responseType(ResponseType.SUCCESS).build();
    }

    public static <T> ApiResponse<T> success(T data, String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .status(status)
                .responseType(ResponseType.SUCCESS)
                .build();
    }

    public static ApiResponse<Object> success(String message, HttpStatus status) {
        return ApiResponse.builder()
                .message(message)
                .status(status)
                .responseType(ResponseType.SUCCESS)
                .build();
    }

    public static ApiResponse<Object> fail(ErrorType errorType, HttpStatus status) {
        return ApiResponse.builder()
                .message(errorType.getMessage())
                .status(status)
                .responseType(ResponseType.FAIL)
                .build();
    }

    public static <T> ApiResponse<T> fail(T data, ErrorType errorType, HttpStatus status) {
        return ApiResponse.<T>builder()
                .message(errorType.getMessage())
                .data(data)
                .status(status)
                .responseType(ResponseType.FAIL)
                .build();
    }

    public static ApiResponse<Object> error(ErrorType errorType, HttpStatus status) {
        return ApiResponse.builder()
                .message(errorType.getMessage())
                .responseType(ResponseType.ERROR)
                .status(status)
                .build();
    }

    public static <T> ApiResponse<T> error(T data, ErrorType errorType, HttpStatus status) {
        return ApiResponse.<T>builder()
                .message(errorType.getMessage())
                .data(data)
                .status(status)
                .responseType(ResponseType.ERROR)
                .build();
    }
}
