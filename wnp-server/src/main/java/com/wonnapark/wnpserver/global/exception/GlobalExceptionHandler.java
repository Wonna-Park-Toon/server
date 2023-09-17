package com.wonnapark.wnpserver.global.exception;

import com.wonnapark.wnpserver.global.response.ErrorCode;
import com.wonnapark.wnpserver.global.response.ErrorResponse;
import com.wonnapark.wnpserver.global.utils.FileUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String UNEXPECTED_ERROR_MESSAGE = "예상치 못한 예외가 발생하였습니다. : ";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        // restTemplate 예외 리팩토링 예정
        log.warn(UNEXPECTED_ERROR_MESSAGE, e);
        return ErrorResponse.create(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(EntityNotFoundException e) {
        log.warn(e.getMessage(), e);
        return ErrorResponse.create(ErrorCode.ENTITY_NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ErrorResponse.create(ErrorCode.BAD_REQUEST, e.getBindingResult());
    }

    @ExceptionHandler(FileUtils.FileIOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(FileUtils.FileIOException e) {
        log.warn(e.getMessage(), e);
        return ErrorResponse.create(ErrorCode.INTERNAL_SERVER_ERROR);
    }

}
