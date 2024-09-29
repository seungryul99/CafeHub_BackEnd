package com.cafehub.backend.common.dto;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"success", "code", "message", "data"})
public class ResponseDTO<T> {

    private final Boolean success;

    private final String code;

    private final String ErrorMessage;

    private final T data;



    public static <T> ResponseDTO<T>  success(T data){

        return new ResponseDTO<T>(true, HttpStatus.OK.toString(), null, data);
    }
}