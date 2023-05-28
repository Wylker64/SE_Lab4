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

    @GetMapping("")
    public Product product(@PathVariable(name = "id") @NotNull Long productId) {
        return ProductService.getProduct(productId);
    }

}
