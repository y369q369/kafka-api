package com.example.kafkaapi.controller;

import com.example.kafkaapi.model.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author grassPrince
 * @Date 2020/11/9
 * @Description 全局异常处理
 **/
@RestControllerAdvice
@Slf4j
public class GlobalException {

    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value =NullPointerException.class)
    public ResponseVO exceptionHandler(HttpServletRequest req, NullPointerException e){
        log.error("空指针异常，原因是：",e);
        return ResponseVO.fail(e.getMessage());
    }

    /**
     * 处理所有异常
     */
    @ExceptionHandler
    public ResponseVO exceptionHandler (HttpServletRequest request, Exception e) {
        log.error("未知异常，原因是：", e);
        return ResponseVO.fail(e.getMessage());
    }

}
