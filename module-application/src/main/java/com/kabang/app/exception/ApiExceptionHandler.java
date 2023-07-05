package com.kabang.app.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kabang.common.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private Integer code;
        private String message;
        private List<BindingResultError> bindingErrorMessage;
        private String path;
        private String stackTrace;

        @JsonIgnore
        private HttpStatus httpStatus;
    }

    @Builder
    @Getter
    public static class BindingResultError{
        private String field;
        private String message;
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> methodValidException(BindException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .bindingErrorMessage(getBindingResultErrors(exception.getBindingResult()))
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .bindingErrorMessage(getBindingResultErrors(exception.getBindingResult()))
                        .build());
    }

    private List<BindingResultError> getBindingResultErrors(BindingResult bindingResult){
        return bindingResult.getFieldErrors().stream()
                .map(fieldError -> BindingResultError.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException exception) {
        return ResponseEntity
                .status(exception.getApiExceptionCode().getHttpStatus())
                .body(ErrorResponse.builder()
                        .code(exception.getApiExceptionCode().getCode())
                        .message(exception.getApiExceptionCode().getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception, WebRequest request) {
        return ResponseEntity
                .status(MissingRequestValueException.class.isAssignableFrom(exception.getClass()) ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .path(request.getDescription(false))
                        .message(exception.getMessage())
                        .stackTrace(ExceptionUtils.getStackTrace(exception))
                        .build());
    }
}
