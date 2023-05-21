package com.segroup2023.lab.controller.account;

import com.segroup2023.lab.database.entity.Account;
import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.exception.type.BadRequestException;
import com.segroup2023.lab.exception.type.LoginFailureException;
import com.segroup2023.lab.exception.type.PermissionDeniedException;
import com.segroup2023.lab.service.AccountService;
import com.segroup2023.lab.service.UserAuthorization;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    private static class Request {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
    }
    private static class Response {
        public Account account;
    }

    @PostMapping("/account")
    public Response account(@RequestBody @NotNull @Valid Request request) throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        Response response = new Response();
        response.account = AccountService.getPersonalAccount(user.getId());
        return response;
    }

    @PostMapping("/account/profit")
    public Response adminProfit(@RequestBody @NotNull @Valid Request request)
        throws LoginFailureException, PermissionDeniedException, BadRequestException {
        User user = UserAuthorization.authorize(request.authorize);
        if(!user.getIdentity().equals(User.Identity.ADMIN))
            throw new PermissionDeniedException(user.getIdentity(), User.Identity.ADMIN);
        Response response = new Response();
        response.account = AccountService.getAdminProfit();
        return response;
    }

    @PostMapping("/account/intermediate")
    public Response adminIntermediate(@RequestBody @NotNull @Valid Request request)
            throws LoginFailureException, PermissionDeniedException, BadRequestException {
        User user = UserAuthorization.authorize(request.authorize);
        if(!user.getIdentity().equals(User.Identity.ADMIN))
            throw new PermissionDeniedException(user.getIdentity(), User.Identity.ADMIN);
        Response response = new Response();
        response.account = AccountService.getAdminPersonal();
        return response;
    }
}
