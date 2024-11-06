package com.cafehub.backend.common.exception.global;


import com.cafehub.backend.common.dto.ResponseDTO;
import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.common.exception.dto.ErrorReason;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{


    @ExceptionHandler(CafeHubException.class)
    public ResponseEntity<ResponseDTO<Void>> handleBusinessException(CafeHubException ex){

        ErrorReason errorReason = ex.getErrorReason();

        return ResponseEntity.status(errorReason.getStatus())
                .body(ResponseDTO.fail(errorReason.getCode(),errorReason.getErrorMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorReason errorReason = CommonErrorCode._INVALID_PARAM_REQUEST.getErrorReason();

        return ResponseEntity.status(errorReason.getStatus())
                .body(ResponseDTO.fail(errorReason.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleMethodArgumentNotValidated(ConstraintViolationException ex){

        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(" && "));

        ErrorReason errorReason = CommonErrorCode._INVALID_PARAM_REQUEST.getErrorReason();

        return ResponseEntity.status(errorReason.getStatus())
                .body(ResponseDTO.fail(errorReason.getCode(), errorMessage));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {

        ErrorReason errorReason = CommonErrorCode._PARAM_TYPE_MISMATCH_REQUEST.getErrorReason();

        return ResponseEntity.status(errorReason.getStatus())
                .body(ResponseDTO.fail(errorReason.getCode(), errorReason.getErrorMessage()));
    }
}
