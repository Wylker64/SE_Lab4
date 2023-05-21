package com.segroup2023.lab.database.entity;

import com.segroup2023.lab.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@AllArgsConstructor
public class OrderItem {
    @Getter @Setter
    private Long id;
    @Getter
    private final Product product;
    @Getter
    private final Long count;
    @Getter
    private final Double cost;

    public OrderItem(OrderItemEntity entity) {
        id = entity.getId();
        product = ProductService.getProduct(entity.getProduct());
        count = entity.getCount();
        cost = entity.getCost();
    }

}
