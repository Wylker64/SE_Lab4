package com.segroup2023.lab.service;

import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.exception.type.LoginFailureException;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Setter;

import java.util.Optional;

public class UserAuthorization {
    @Data
    public static class Request{
        @NotNull
        @Setter
        public Long id;
        @NotNull
        @Setter
        public String username;
    }
    public static User authorize(Request request) throws LoginFailureException {
        Optional<User> optionalUser = UserService.findById(request.id);
        if(optionalUser.isEmpty())
            throw new LoginFailureException(LoginFailureException.Cause.AUTHORIZATION);
        User user = optionalUser.get();
        if(!user.getUsername().equals(request.username))
            throw new LoginFailureException(LoginFailureException.Cause.AUTHORIZATION);
        return user;
    }
}
