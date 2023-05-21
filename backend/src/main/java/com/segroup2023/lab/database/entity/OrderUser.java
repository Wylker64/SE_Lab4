package com.segroup2023.lab.database.entity;

import com.segroup2023.lab.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ResponseBody
@AllArgsConstructor
public class OrderUser {
    @Getter @Setter
    private Long id;
    @Getter
    private final Date time;
    @Getter
    private final Double cost;
    @Getter
    private final Boolean paid, deleted;
    @Getter
    private final List<OrderShop> orders;

    public OrderUser(OrderUserEntity entity) {
        id = entity.getId();
        time = entity.getTime();
        cost = entity.getCost();
        paid = entity.getPaid();
        deleted = entity.getCanceled();
        orders = new ArrayList<>();
        List<OrderShopEntity> shopEntities = OrderService.findByOrderUser(entity.getId());
        for (OrderShopEntity shopEntity: shopEntities) {
            orders.add(new OrderShop(shopEntity));
        }
    }

}
