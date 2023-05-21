package com.segroup2023.lab.database.entity;

import com.segroup2023.lab.service.ProductService;
import com.segroup2023.lab.service.ShopService;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class CartItem {
    @Getter
    private final Long id;
    @Getter
    private final Long product_id;
    @Getter
    private final String shop_name;
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final Double price;
    @Getter
    private final Integer image_num;
    @Getter
    private final Long count;
    @Getter
    private final boolean valid;

    public CartItem(CartItemEntity entity) {
        Product product = ProductService.getProduct(entity.getProductId());
        id = entity.getId();
        product_id = entity.getProductId();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        image_num = product.getImage_num();
        count = entity.getCount();
        valid = entity.getValid();
        if(valid) {
            shop_name = ShopService.findById(product.getShopId()).getName();
        } else {
            shop_name = null;
        }
    }
}
