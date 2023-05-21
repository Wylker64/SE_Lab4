package com.segroup2023.lab.controller;

import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.exception.type.FieldConflictException;
import com.segroup2023.lab.exception.type.LoginFailureException;
import com.segroup2023.lab.service.UserAuthorization;
import com.segroup2023.lab.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private static class RegisterRequest {
        @NotNull
        public String username;
        @NotNull
        public String phone_number;
        @NotNull
        public String id_card;
        @NotNull
        public String email;
        @NotNull
        public String password;
        @NotNull
        @Min(value = 0, message = "Identity less than 0")
        @Max(value = 1, message = "Identity larger than 1")
        public Integer identity;
    }

    private static class LoginRequest {
        @NotNull
        public String username;
        @NotNull
        public String password;
    }

    private static class ModifyRequest {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public String username, phone_number, email, current_password;
        public String password;
    }

    private static class InfoRequest {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
    }

    private static class Response {
        public Long userid;
        public int identity;
    }

    @PostMapping("/register")
    public Response register(@RequestBody @Valid @NotNull RegisterRequest request) throws FieldConflictException {
        Response response = new Response();
        User user = new User(request.username, request.phone_number,
                request.id_card, request.email,
                request.password, User.Identity.values()[request.identity]);
        UserService.register(user);
        response.userid = user.getId();
        response.identity = user.getIdentity().ordinal();
        return response;
    }

    @PostMapping("/login")
    public Response login(@RequestBody @Valid @NotNull LoginRequest request) throws LoginFailureException {
        User user = UserService.login(request.username, request.password);
        Response response = new Response();
        response.identity = user.getIdentity().ordinal();
        response.userid = user.getId();
        return response;
    }

    @PostMapping("/modify")
    public void modify(@RequestBody @Valid @NotNull ModifyRequest request)
            throws LoginFailureException, FieldConflictException {
        User user = UserAuthorization.authorize(request.authorize);
        if(!user.getPassword().equals(request.current_password))
            throw new LoginFailureException(LoginFailureException.Cause.PASSWORD);
        UserService.modify(user, request.username, request.phone_number, request.email, request.password);
    }

    @PostMapping("/info")
    public User info(@RequestBody @Valid @NotNull InfoRequest request) throws LoginFailureException {
        return UserAuthorization.authorize(request.authorize);
    }
}