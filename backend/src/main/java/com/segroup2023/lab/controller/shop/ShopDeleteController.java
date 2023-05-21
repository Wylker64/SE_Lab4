package com.segroup2023.lab.controller.shop;

import com.segroup2023.lab.database.entity.Shop;
import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.exception.type.BadRequestException;
import com.segroup2023.lab.exception.type.LoginFailureException;
import com.segroup2023.lab.exception.type.PermissionDeniedException;
import com.segroup2023.lab.service.ShopService;
import com.segroup2023.lab.service.UserAuthorization;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShopDeleteController {
    private static class ApplicationRequest {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public Long shop_id;
    }

    private static class PageRequest {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public Integer page_num;
    }

    private static class ApprovalRequest {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public Long shop_id;
    }

    private static class PageResponse {
        public int total_pages;
        public List<Shop> shops;
    }

    @PostMapping("/shop/delete")
    public void application(@RequestBody @Valid @NotNull ApplicationRequest request)
        throws LoginFailureException, PermissionDeniedException, BadRequestException {
        User user = UserAuthorization.authorize(request.authorize);
        if(!user.getIdentity().equals(User.Identity.VENDOR))
            throw new PermissionDeniedException(user.getIdentity(), User.Identity.VENDOR);
        Shop shop = ShopService.findById(request.shop_id);
        if(!ShopService.belongTo(user, shop))
            throw new BadRequestException("Shop does not belong to user.");
        ShopService.deleteApply(shop);
    }

    @PostMapping("/shop/delete/review")
    public PageResponse page(@RequestBody @Valid @NotNull PageRequest request)
        throws LoginFailureException, PermissionDeniedException {
        User user = UserAuthorization.authorize(request.authorize);
        if(!user.getIdentity().equals(User.Identity.ADMIN))
            throw new PermissionDeniedException(user.getIdentity(), User.Identity.ADMIN);
        Page<Shop> shopPage = ShopService.findByDeleting(request.page_num - 1, true);
        PageResponse pageResponse = new PageResponse();
        pageResponse.total_pages = shopPage.getTotalPages();
        pageResponse.shops = shopPage.toList();
        return pageResponse;
    }

    @PostMapping("/shop/delete/approval")
    public void approval(@RequestBody @Valid @NotNull ApprovalRequest request)
        throws LoginFailureException, PermissionDeniedException, BadRequestException {
        User user = UserAuthorization.authorize(request.authorize);
        if(!user.getIdentity().equals(User.Identity.ADMIN))
            throw new PermissionDeniedException(user.getIdentity(), User.Identity.ADMIN);
        Shop shop = ShopService.findById(request.shop_id);
        ShopService.deleteApprove(shop);
    }

}
