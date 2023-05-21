package com.segroup2023.lab.exception.handler;

import com.segroup2023.lab.controller.shop.ApplicationController;
import com.segroup2023.lab.controller.shop.ApprovalHandler;
import com.segroup2023.lab.controller.shop.ShopQueryController;
import com.segroup2023.lab.exception.type.FieldConflictException;
import com.segroup2023.lab.exception.type.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {ApplicationController.class, ShopQueryController.class, ApprovalHandler.class})
public class ShopExceptionHandler {
    private static final String controllerType = "Shop:\n";

    @ExceptionHandler(FieldConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String fieldConflict(FieldConflictException e) {
        return e.getField() + " already exists!";
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String itemNotFound(ItemNotFoundException e) {
        return e.getMessage();
    }
}
