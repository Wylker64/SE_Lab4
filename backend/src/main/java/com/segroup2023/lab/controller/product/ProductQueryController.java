package com.segroup2023.lab.controller.product;

import com.segroup2023.lab.database.entity.Product;
import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.exception.type.LoginFailureException;
import com.segroup2023.lab.service.ProductService;
import com.segroup2023.lab.service.UserAuthorization;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/{id}")
public class ProductQueryController {
    private static class SingleRequest {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
    }

    private static class SingleResponse {
        public Product product;
    }

    @PostMapping("")
    public SingleResponse product(@PathVariable(name = "id") @NotNull Long productId, @RequestBody @Valid @NotNull SingleRequest request)
        throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        Product product = ProductService.getProduct(productId);
        if(user.getIdentity().equals(User.Identity.USER) && !product.getApproved())
            return null;
        SingleResponse response = new SingleResponse();
        response.product = product;
        return response;
    }

}
