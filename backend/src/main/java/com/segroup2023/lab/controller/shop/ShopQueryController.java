package com.segroup2023.lab.controller.shop;

import com.segroup2023.lab.database.entity.*;
import com.segroup2023.lab.exception.type.BadRequestException;
import com.segroup2023.lab.exception.type.LoginFailureException;
import com.segroup2023.lab.exception.type.PermissionDeniedException;
import com.segroup2023.lab.service.ProductService;
import com.segroup2023.lab.service.ShopService;
import com.segroup2023.lab.service.UserAuthorization;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShopQueryController {
    private static class PageRequest {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public Integer page_num;
        public boolean review;
    }

    private static class SingleRequest {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public int product_page_num;
        public int record_page_num;
    }

    private static class PageResponse {
        public User.Identity identity;
        public int total_pages;
        public List<Shop> shops;
    }

    private static class SingleResponse {
        public Shop shop;
        public Account account;
        public List<Product> products;
        public List<ProductInfo> records;
        public int product_total_pages, record_total_pages;
    }

    @PostMapping("/shop")
    public PageResponse shopPages(@RequestBody @Valid @NotNull ShopQueryController.PageRequest request)
            throws LoginFailureException, PermissionDeniedException {
        User user = UserAuthorization.authorize(request.authorize);
        PageResponse pageResponse = new PageResponse();
        if(request.page_num <= 0)
            request.page_num = 1;
        User.Identity identity = user.getIdentity();
        pageResponse.identity = identity;
        Page<Shop> shopPage;
        if(!request.review)
            shopPage = ShopService.findByApprovedTrue(request.page_num - 1);
        else {
            if(identity.equals(User.Identity.ADMIN))
                shopPage = ShopService.findByApprovedFalse(request.page_num - 1);
            else
                throw new PermissionDeniedException(identity, User.Identity.ADMIN);
        }
        pageResponse.total_pages = shopPage.getTotalPages();
        pageResponse.shops = shopPage.toList();
        return pageResponse;
    }

    @PostMapping("/shop/vendor")
    public PageResponse shopPagesVendor(@RequestBody @Valid @NotNull ShopQueryController.PageRequest request)
        throws LoginFailureException, PermissionDeniedException {
        User user = UserAuthorization.authorize(request.authorize);
        if(!user.getIdentity().equals(User.Identity.VENDOR))
            throw new PermissionDeniedException(user.getIdentity(), User.Identity.VENDOR);
        Page<Shop> shopPage = ShopService.findByOwner(request.page_num - 1, user.getId());
        PageResponse pageResponse = new PageResponse();
        pageResponse.total_pages = shopPage.getTotalPages();
        pageResponse.shops = shopPage.toList();
        return pageResponse;
    }

    @PostMapping("/shop/{id}/vendor")
    public SingleResponse shopVendor(@PathVariable Long id, @RequestBody @Valid @NotNull SingleRequest request)
        throws LoginFailureException, BadRequestException {
        User user = UserAuthorization.authorize(request.authorize);
        Shop shop = ShopService.findById(id);
        if(!ShopService.belongTo(user, shop))
            throw new BadRequestException("Shop does not belong to user.");
        Account account = ShopService.getAccount(shop);
        Page<Product> products = ProductService.getProducts(request.product_page_num - 1, id, User.Identity.VENDOR);
        Page<ProductInfo> records = ProductService.getRecords(request.record_page_num - 1, id);
        SingleResponse response = new SingleResponse();
        response.shop = shop;
        response.account = account;
        response.product_total_pages = products.getTotalPages();
        response.record_total_pages = records.getTotalPages();
        response.products = products.toList();
        response.records = records.toList();
        return response;
    }

    @PostMapping("/shop/{id}")
    public SingleResponse shop(@PathVariable Long id, @RequestBody @Valid @NotNull SingleRequest request)
            throws LoginFailureException, BadRequestException {
        User user = UserAuthorization.authorize(request.authorize);
        Shop shop = ShopService.findById(id);
        Account account = ShopService.getAccount(shop);
        Page<Product> products = ProductService.getProducts(request.product_page_num - 1, id, User.Identity.USER);
        SingleResponse response = new SingleResponse();
        response.shop = shop;
        response.account = account;
        response.product_total_pages = products.getTotalPages();
        response.products = products.toList();
        return response;
    }

}
