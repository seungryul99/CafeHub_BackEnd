package com.cafehub.backend.common.exception.global;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{


    @ExceptionHandler(CafeHubException.class)
    public ResponseEntity<ResponseDTO<Void>> handleBusinessException(CafeHubException ex){

        ErrorReason errorReason = ex.getErrorReason();

        log.warn("비즈니스 예외 발생 : {}", errorReason.getCode());

        return ResponseEntity.status(errorReason.getStatus())
                .body(ResponseDTO.fail(errorReason));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorReason errorReason = CommonErrorCode._INVALID_PARAM_REQUEST.getErrorReason();

        log.warn("파라미터 검증 중 예외 발생 : {}", errorReason.getCode());

        return ResponseEntity.status(errorReason.getStatus())
                .body(ResponseDTO.fail(errorReason));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleMethodArgumentNotValidated(ConstraintViolationException ex){

        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(" , "));
        errorMessage += ".";

        ErrorReason errorReason = CommonErrorCode._INVALID_PARAM_REQUEST.getErrorReason();

        log.warn("파라미터 검증 중 예외 발생 : {}", errorReason.getCode());

        return ResponseEntity.status(errorReason.getStatus())
                .body(ResponseDTO.fail(errorReason.getCode(), errorMessage));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {

        ErrorReason errorReason = CommonErrorCode._PARAM_TYPE_MISMATCH_REQUEST.getErrorReason();

        log.warn("파라미터 검증 중 예외 발생 : {}", errorReason.getCode());

        return ResponseEntity.status(errorReason.getStatus())
                .body(ResponseDTO.fail(errorReason));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorReason errorReason = CommonErrorCode._METHOD_NOT_ALLOWED.getErrorReason();

        log.warn("허용되지 않은 HTTP 메서드 요청으로 예외 발생 : {}", errorReason.getCode());

        return ResponseEntity.status(errorReason.getStatus())
                .body(ResponseDTO.fail(errorReason));
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorReason errorReason = CommonErrorCode._NOT_FOUND.getErrorReason();

        log.warn("Request URL 이상으로 리소스를 찾을 수 없는 예외 발생 : {}", errorReason.getCode());

        return ResponseEntity.status(status)
                .body(ResponseDTO.fail(errorReason));
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        log.warn("ResponseEntityExceptionHandler가 처리한 스프링 MVC 예외 발생 : {}",statusCode);

        return ResponseEntity.status(statusCode)
                .body(ResponseDTO.fail(statusCode.toString(), ex.getMessage()));
    }





    // 스프링에서 기본적으로 제공하는 ResponseEntityExceptionHandler와 내가 직접 처리해주는 CafeHubException 이나 Validation을 처리해도
    // 내가 놓친 알 수 없는 원인으로 추가적인 에러가 발생할 수 있음, 따라서 최종 방어선을 만들어 놓음
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ResponseDTO<Void>> handleUnknownInternalException(){

        ErrorReason errorReason = CommonErrorCode._UNKNOWN_INTERNAL_SERVER_ERROR.getErrorReason();

        log.warn("핸들러에서 알 수 없는 처리하지 못한 예외 발생 : {}", errorReason.getCode());

        return ResponseEntity.status(errorReason.getStatus())
                .body(ResponseDTO.fail(errorReason));
    }

}
