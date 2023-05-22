package com.segroup2023.lab.controller;

import com.segroup2023.lab.database.entity.*;
import com.segroup2023.lab.exception.type.InsufficientBalanceException;
import com.segroup2023.lab.exception.type.LoginFailureException;
import com.segroup2023.lab.service.OrderService;
import com.segroup2023.lab.service.UserAuthorization;
import com.segroup2023.lab.utils.ApplyStatus;
import com.segroup2023.lab.utils.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/checkout")
public class OrderController {
    private static class CreateRequest {
        @NotNull @Valid
        public UserAuthorization.Request authorize;
        @NotNull
        public Long address_id;
        @NotNull
        public List<OrderService.OrderItemRequest> items;
        @NotNull
        public Boolean create;
    }

    @PostMapping("/create")
    public static OrderUser createByCart(@RequestBody @Valid @NotNull CreateRequest request)
            throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        OrderUser orderUser = OrderService.order(user.getId(), request.address_id, request.items);
        if (request.create)
            OrderService.save(orderUser, user.getId(), request.address_id);
        return orderUser;
    }

    private static class ViewUserRequest {
        @NotNull @Valid
        public UserAuthorization.Request authorize;
        public String status;
    }

    @PostMapping("/user")
    public static List<OrderShop> getOrderShopUser(@RequestBody @Valid @NotNull ViewUserRequest request)
        throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        List<OrderShopEntity> entityList;
        if (request.status == null)
            entityList = OrderService.getOrderUser(user.getId(), null);
        else
            entityList = OrderService.getOrderUser(user.getId(), OrderStatus.valueOf(request.status));
        List<OrderShop> orders = new ArrayList<>();
        for (OrderShopEntity entity: entityList) {
            orders.add(new OrderShop(entity));
        }
        return orders;
    }

    private static class ViewShopRequest {
        @NotNull @Valid
        public UserAuthorization.Request authorize;
        @NotNull
        public Long shop_id;
        public String order_status, refund_status;
    }

    @PostMapping("/shop")
    public static List<OrderShop> getOrderShop(@RequestBody @Valid @NotNull ViewShopRequest request)
            throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        List<OrderShopEntity> entityList = null;
        if (request.order_status != null) {
            entityList = OrderService.getOrderShop(request.shop_id, OrderStatus.valueOf(request.order_status));
        }
        if (request.refund_status != null) {
            entityList = OrderService.getOrderShop(request.shop_id, ApplyStatus.valueOf(request.refund_status));
        }
        assert entityList != null;
        List<OrderShop> orders = new ArrayList<>();
        for (OrderShopEntity entity: entityList) {
            orders.add(new OrderShop(entity));
        }
        return orders;
    }

    private static class UpdateRequest {
        @NotNull @Valid
        public UserAuthorization.Request authorize;
        @NotNull
        public Long order_id;
    }

    @PostMapping("/pay")
    public static void pay(@RequestBody @NotNull @Valid UpdateRequest request)
            throws LoginFailureException, InsufficientBalanceException {
        User user = UserAuthorization.authorize(request.authorize);
        OrderService.pay(request.order_id);
    }

    @PostMapping("/send")
    public static void send(@RequestBody @NotNull @Valid UpdateRequest request) throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        OrderService.send(request.order_id);
    }

    @PostMapping("/receive")
    public static void receive(@RequestBody @NotNull @Valid UpdateRequest request) throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        OrderService.receive(request.order_id);
    }

    @PostMapping("/refund")
    public static void refund(@RequestBody @NotNull @Valid UpdateRequest request) throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        OrderService.applyRefund(request.order_id);
    }

    @PostMapping("/refund/approve")
    public static void refundApprove(@RequestBody @NotNull @Valid UpdateRequest request) throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        OrderService.approveRefund(request.order_id);
    }

    @PostMapping("/refund/deny")
    public static void refundDeny(@RequestBody @NotNull @Valid UpdateRequest request) throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        OrderService.denyRefund(request.order_id);
    }

    @PostMapping("/cancel")
    public static void cancel(@RequestBody @NotNull @Valid UpdateRequest request) throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        OrderService.cancel(request.order_id);
    }

    @PostMapping("/delete")
    public static void deleteShop(@RequestBody @NotNull @Valid UpdateRequest request) throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        OrderService.deleteOrderShop(request.order_id);
    }

}
