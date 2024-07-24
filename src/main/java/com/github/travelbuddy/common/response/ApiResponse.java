package com.github.travelbuddy.common.response;

import com.github.travelbuddy.common.enums.ErrorType;
import com.github.travelbuddy.common.enums.ResponseType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    private ResponseType responseType;
    private String message;
    private T data;

    public static ApiResponse<Object> success() {
        return ApiResponse.builder().responseType(ResponseType.SUCCESS).build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .responseType(ResponseType.SUCCESS)
                .build();
    }

    public static ApiResponse<Object> success(String message) {
        return ApiResponse.builder()
                .message(message)
                .responseType(ResponseType.SUCCESS)
                .build();
    }

    public static ApiResponse<Object> fail(ErrorType errorType) {
        return ApiResponse.builder()
                .message(errorType.getMessage())
                .responseType(ResponseType.FAIL)
                .build();
    }

    public static <T> ApiResponse<T> fail(T data, ErrorType errorType) {
        return ApiResponse.<T>builder()
                .message(errorType.getMessage())
                .data(data)
                .responseType(ResponseType.FAIL)
                .build();
    }

    public static ApiResponse<Object> error(ErrorType errorType) {
        return ApiResponse.builder()
                .message(errorType.getMessage())
                .responseType(ResponseType.ERROR)
                .build();
    }

    public static <T> ApiResponse<T> error(T data, ErrorType errorType) {
        return ApiResponse.<T>builder()
                .message(errorType.getMessage())
                .data(data)
                .responseType(ResponseType.ERROR)
                .build();
    }

//    public static ApiResponse<MypageResponse> error(String message, ErrorType errorType) {
//        return ApiResponse.<MypageResponse>builder()
//                .message(message)
//                .data(null) // MypageResponse 타입의 데이터가 없으므로 null 처리
//                .responseType(ResponseType.ERROR)
//                .build();
//    }
}
