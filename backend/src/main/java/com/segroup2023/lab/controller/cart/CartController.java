package com.segroup2023.lab.controller.cart;


import com.segroup2023.lab.database.entity.CartItem;
import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.exception.type.LoginFailureException;
import com.segroup2023.lab.service.CartService;
import com.segroup2023.lab.service.UserAuthorization;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController
{
    private static class CreateRequest {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public Long product_id;
        @NotNull
        public Long count;
    }

    private static class QueryRequest {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public int page_num;
    }

    private static class QueryResponse {
        public List<CartItem> items;
        public int total_pages;
    }

    private static class DeleteRequest {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        public Long item_id;
        public Long[] item_id_list;
    }

    @PostMapping("/create")
    public void addToCart(@RequestBody @Valid @NotNull CreateRequest request)
        throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        CartService.addToCart(user.getId(), request.product_id, request.count);
    }


    @PostMapping("")
    public QueryResponse viewCart(@RequestBody @Valid @NotNull QueryRequest request)
        throws LoginFailureException{
        User user = UserAuthorization.authorize(request.authorize);
        Page<CartItem> items = CartService.getCartItems(user.getId(), request.page_num - 1);
        QueryResponse response = new QueryResponse();
        response.items = items.toList();
        response.total_pages = items.getTotalPages();
        return response;
    }

    @PostMapping("/delete")
    public void removeFromCart(@RequestBody @Valid @NotNull DeleteRequest request)
        throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        if(request.item_id != null) {
            CartService.removeCartItem(user.getId(), request.item_id);
        }
        if(request.item_id_list != null) {
            CartService.removeCartItem(user.getId(), request.item_id_list);
        }
    }

}


