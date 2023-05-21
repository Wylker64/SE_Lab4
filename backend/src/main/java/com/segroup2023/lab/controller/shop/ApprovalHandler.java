package com.segroup2023.lab.controller.shop;

import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.exception.type.*;
import com.segroup2023.lab.service.ShopService;
import com.segroup2023.lab.service.UserAuthorization;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApprovalHandler {
    private static class Request {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public Long shop_id;
        @NotNull
        public Boolean approved;
    }

    @PostMapping("/shop/approval")
    public User.Identity approve(@RequestBody @Valid @NotNull Request request)
            throws LoginFailureException, PermissionDeniedException, ItemNotFoundException,
            BadRequestException, InsufficientBalanceException {
        User user = UserAuthorization.authorize(request.authorize);
        if(!user.getIdentity().equals(User.Identity.ADMIN))
            throw new PermissionDeniedException(user.getIdentity(), User.Identity.ADMIN);
        ShopService.approve(user, request.shop_id, request.approved);
        return user.getIdentity();
    }
}
