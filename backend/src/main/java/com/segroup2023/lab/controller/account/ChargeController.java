package com.segroup2023.lab.controller.account;

import com.segroup2023.lab.database.entity.Account;
import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.exception.type.BadRequestException;
import com.segroup2023.lab.exception.type.LoginFailureException;
import com.segroup2023.lab.service.AccountService;
import com.segroup2023.lab.service.UserAuthorization;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChargeController {
    private static class Request {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public Long account_id;
        @NotNull
        public Double amount;
    }

    @PostMapping("/account/charge")
    public void charge(@RequestBody @Valid @NotNull Request request) throws LoginFailureException, BadRequestException {
        User user = UserAuthorization.authorize(request.authorize);
        Account account = AccountService.findById(request.account_id);
        if(!AccountService.belongTo(user, account))
            throw new BadRequestException("Account does not belong to user!");
        AccountService.charge(account, request.amount);
    }
}
