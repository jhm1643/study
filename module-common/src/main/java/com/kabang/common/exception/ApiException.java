package com.kabang.common.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{

    private ApiExceptionCode apiExceptionCode;

    public ApiException(ApiExceptionCode apiExceptionCode){
        this.apiExceptionCode = apiExceptionCode;
    }
}
