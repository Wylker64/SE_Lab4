package com.segroup2023.lab.exception.handler;

import com.segroup2023.lab.controller.UserController;
import com.segroup2023.lab.exception.type.FieldConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice(assignableTypes = UserController.class)
//@ControllerAdvice
public class UserExceptionHandler {

    private static final String controllerType = "Account:\n";

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String SQLConstraintException(SQLIntegrityConstraintViolationException e) {
        return controllerType+e.getMessage();
    }

    @ExceptionHandler(FieldConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String fieldConflict(FieldConflictException e) {
        return e.getField()+" already exists!";
    }

}
