package com.segroup2023.lab.controller.product;

import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.exception.type.LoginFailureException;
import com.segroup2023.lab.service.CartService;
import com.segroup2023.lab.service.ProductService;
import com.segroup2023.lab.service.ShopService;
import com.segroup2023.lab.service.UserAuthorization;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/delete")
public class ProductDeleteController {
    private static class Request {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public Long product_id;
    }

    @PostMapping("")
    public void delete(@RequestBody @Valid @NotNull Request request) throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        if(!ShopService.belongTo(user.getId(), ProductService.getShopId(request.product_id)))
            return;
        ProductService.delete(request.product_id);
        CartService.updateDeletedProduct(request.product_id);
    }

}
