package com.segroup2023.lab.database.entity;

import com.segroup2023.lab.service.AddressService;
import com.segroup2023.lab.service.OrderService;
import com.segroup2023.lab.service.ShopService;
import com.segroup2023.lab.service.UserService;
import com.segroup2023.lab.utils.ApplyStatus;
import com.segroup2023.lab.utils.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ResponseBody
@AllArgsConstructor
public class OrderShop {
    @Getter @Setter
    private Long id;
    @Getter
    private final List<OrderItem> items;
    @Getter
    private final Shop shop;
    @Getter
    private final Double cost, discount;
    @Getter
    private final User user;
    @Getter
    private final Address address;
    @Getter
    private final Date time;
    @Getter
    private final OrderStatus status;
    @Getter
    private final ApplyStatus refund;

    public OrderShop(OrderShopEntity entity) {
        id = entity.getId();
        items = new ArrayList<>();
        List<OrderItemEntity> entities = OrderService.findByOrderShop(id);
        for (OrderItemEntity itemEntity: entities) {
            items.add(new OrderItem(itemEntity));
        }
        shop = ShopService.findById(entity.getShop());
        cost = entity.getCost();
        discount = entity.getDiscount();
        OrderUserEntity userEntity = OrderService.getUserEntity(entity.getOrderUser());
        user = UserService.getUser(userEntity.getUser());
        address = AddressService.getAddress(userEntity.getAddress());
        time = userEntity.getTime();
        status = entity.getStatus();
        refund = entity.getRefund();
    }

}
