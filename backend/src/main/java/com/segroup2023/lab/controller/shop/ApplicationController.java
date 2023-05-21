package com.segroup2023.lab.controller.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.segroup2023.lab.database.entity.Shop;
import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.exception.type.*;
import com.segroup2023.lab.service.ShopService;
import com.segroup2023.lab.service.UserAuthorization;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ApplicationController {
    private static class Request {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public String shop_name;
        @NotNull
        public String type;
        @NotNull
        public String id_card;
        @NotNull
        public String description;
        @NotNull
        public String address;
        @NotNull
        public Double capital;
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        public Date date;
    }

    @PostMapping("/shop/application")
    public User.Identity application(@RequestBody @Valid @NotNull Request request)
            throws LoginFailureException, FieldConflictException, PermissionDeniedException,
            InsufficientBalanceException, BadRequestException {
        User user = UserAuthorization.authorize(request.authorize);
        if(!user.getIdentity().equals(User.Identity.VENDOR))
            throw new PermissionDeniedException(user.getIdentity(), User.Identity.VENDOR);
        Shop shop = new Shop(user, request.shop_name,
                request.type, request.id_card,
                request.description, request.address,
                request.capital, request.date);
        ShopService.apply(user, shop);
        return user.getIdentity();
    }

}
