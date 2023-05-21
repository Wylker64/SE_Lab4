package com.segroup2023.lab.exception.handler;

import com.segroup2023.lab.exception.type.BadRequestException;
import com.segroup2023.lab.exception.type.InsufficientBalanceException;
import com.segroup2023.lab.exception.type.LoginFailureException;
import com.segroup2023.lab.exception.type.PermissionDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String usernameNotExist(LoginFailureException e){
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String argumentNotValid(MethodArgumentNotValidException e) {
        return e.getMessage();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequest(BadRequestException e) {
        log.warn(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String insufficientBalance(InsufficientBalanceException e) {
        return e.getMessage();
    }

    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String permissionDenied(PermissionDeniedException e) {
        return e.getMessage();
    }
}
