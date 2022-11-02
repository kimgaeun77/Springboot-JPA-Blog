package com.cos.blog.controller.handler;

import com.cos.blog.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice//모든 전역에서 exception이 발생하면 이 클래스에서 처리해줌 + return 타입이 객체로 가능!(RequestBody어노테이션이 있음)
//@RestController
public class GlobalExcetionHandler {

    @ExceptionHandler(value = Exception.class)//어떤 예외를 처리할 것인가
    public ResponseDto<String> handleArgumentExcetion(Exception e){
        return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
    }
}
