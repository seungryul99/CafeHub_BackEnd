package com.cafehub.backend.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"success", "data", "errorCode"})
public class ResponseDTO <T>{

    private Boolean success;

    private  T data;

    private ErrorCode errorCode;


    public static <T> ResponseDTO<T>  success(T data){

        return new ResponseDTO<>(true,data,null);
    }

    public static <T> ResponseDTO<T> fail(ErrorCode errorCode){
        return new ResponseDTO<>(false,null,errorCode);
    }
}


