package com.kabang.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApiExceptionCode {

    //HTTP STATUS CODE 500
    EXTERNAL_API_ERROR(500, 5001, "현재 카카오 또는 네이버 API 이용이 불가능합니다. 관리자에게 문의해주세요."),
    INTERNAL_SERVER_ERROR(500, 5003, "서버 에러를 총칭하는 에러 코드로, 요청을 처리하는 과정에서 서버가 예상하지 못한 상황에 놓인 상태입니다.");

    private int httpStatus;
    private int code;
    private String message;
}
