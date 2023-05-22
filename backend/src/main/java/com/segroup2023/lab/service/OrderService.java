package com.segroup2023.lab.service;

import com.segroup2023.lab.database.entity.*;
import com.segroup2023.lab.database.repository.OrderItemRepository;
import com.segroup2023.lab.database.repository.OrderShopRepository;
import com.segroup2023.lab.database.repository.OrderUserRepository;
import com.segroup2023.lab.exception.type.InsufficientBalanceException;
import com.segroup2023.lab.utils.ApplyStatus;
import com.segroup2023.lab.utils.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class OrderService {
    private static OrderItemRepository itemRepository;
    private static OrderShopRepository shopRepository;
    private static OrderUserRepository userRepository;

    @AllArgsConstructor
    public static class OrderItemRequest {
        @NotNull
        @Getter @Setter
        private Long product_id, count;
    }

    @Autowired
    private OrderService(OrderItemRepository itemRepository, OrderShopRepository shopRepository, OrderUserRepository userRepository) {
        OrderService.itemRepository = itemRepository;
        OrderService.shopRepository = shopRepository;
        OrderService.userRepository = userRepository;
    }

    public static OrderUser order(Long userId, Long addressId, List<OrderItemRequest> items) {
        User user = UserService.getUser(userId);
        Address address = AddressService.getAddress(addressId);
        Date time = new Date();
        List<OrderShop> orderShops = new ArrayList<>();
        double userOrderCost = 0.0;
        Set<Long> shops = new HashSet<>();
        for (OrderItemRequest item: items) {
            shops.add(ProductService.getProduct(item.getProduct_id()).getShopId());
        }
        for (Long shopId: shops) {
            Shop shop = ShopService.findById(shopId);
            List<OrderItem> orderItems = new ArrayList<>();
            double shopOrderCost = 0.0;
            for (OrderItemRequest itemRequest: items) {
                Product product = ProductService.getProduct(itemRequest.getProduct_id());
                if (product.getShopId().equals(shopId)) {
                    double cost = product.getPrice() * itemRequest.getCount();
                    shopOrderCost += cost;
                    orderItems.add(new OrderItem(null, product, itemRequest.getCount(), cost));
                }
            }
            userOrderCost += shopOrderCost;
            orderShops.add(new OrderShop(null, orderItems, shop, shopOrderCost, user, address, time, OrderStatus.PAY, ApplyStatus.WAITING));
        }
        return new OrderUser(null, time, userOrderCost, false, false, orderShops);
    }

    public static Long save(OrderUser orderUser, Long userId, Long addressId) {
        OrderUserEntity userEntity = new OrderUserEntity(null, userId, addressId, orderUser.getTime(),
                orderUser.getCost(), orderUser.getPaid(), orderUser.getDeleted());
        userRepository.save(userEntity);
        orderUser.setId(userEntity.getId());
        for (OrderShop orderShop: orderUser.getOrders()) {
            OrderShopEntity shopEntity = new OrderShopEntity(null, userEntity.getId(), orderShop.getShop().getId(),
                    orderShop.getCost(), orderShop.getStatus(), orderShop.getRefund());
            shopRepository.save(shopEntity);
            orderShop.setId(shopEntity.getId());
            for (OrderItem orderItem: orderShop.getItems()) {
                OrderItemEntity itemEntity = new OrderItemEntity(null, shopEntity.getId(), orderItem.getProduct().getId(),
                        orderItem.getCount(), orderItem.getCost());
                itemRepository.save(itemEntity);
                orderItem.setId(itemEntity.getId());
            }
        }
        return userEntity.getId();
    }

    public static void pay(Long orderUserId) throws InsufficientBalanceException {
        OrderUserEntity userEntity = getUserEntity(orderUserId);
        AccountService.transferToAdmin(userEntity.getUser(), userEntity.getCost());
        userEntity.setPaid(true);
        userRepository.save(userEntity);
        setOrderShopStatus(orderUserId, OrderStatus.SEND);
    }

    public static void send(Long orderShopId) {
        OrderShopEntity shopEntity = getShopEntity(orderShopId);
        shopEntity.setStatus(OrderStatus.RECEIVE);
        shopRepository.save(shopEntity);
    }

    public static void receive(Long orderShopId) {
        OrderShopEntity shopEntity = getShopEntity(orderShopId);
        assert shopEntity.getRefund() == ApplyStatus.NONE || shopEntity.getRefund() == ApplyStatus.DENIED;
        Double cost = shopEntity.getCost();
        AccountService.adminProfit(0.05 * cost);
        AccountService.shopProfit(shopEntity.getShop(), (1-0.05) * cost);
        shopEntity.setStatus(OrderStatus.DONE);
        shopRepository.save(shopEntity);
    }

    public static void applyRefund(Long orderShopId) {
        OrderShopEntity shopEntity = getShopEntity(orderShopId);
        shopEntity.setRefund(ApplyStatus.WAITING);
        shopRepository.save(shopEntity);
    }

    public static void denyRefund(Long orderShopId) {
        OrderShopEntity shopEntity = getShopEntity(orderShopId);
        shopEntity.setRefund(ApplyStatus.DENIED);
        shopRepository.save(shopEntity);
    }

    public static void approveRefund(Long orderShopId) {
        OrderShopEntity shopEntity = getShopEntity(orderShopId);
        shopEntity.setRefund(ApplyStatus.APPROVED);
        shopEntity.setStatus(OrderStatus.REFUNDED);
        shopRepository.save(shopEntity);
        AccountService.undoTransferToAdmin(getUserEntity(shopEntity.getOrderUser()).getUser(), shopEntity.getCost());
    }

    public static void cancel(Long orderUserId) {
        OrderUserEntity userEntity = getUserEntity(orderUserId);
        assert !userEntity.getPaid():"Order already paid.";
        userEntity.setCanceled(true);
        userRepository.save(userEntity);
        setOrderShopStatus(orderUserId, OrderStatus.CANCELED);
    }

    private static void setOrderShopStatus(Long orderUserId, OrderStatus status) {
        List<OrderShopEntity> shopEntities = shopRepository.findByOrderUser(orderUserId);
        for (OrderShopEntity shopEntity : shopEntities) {
            shopEntity.setStatus(status);
            shopRepository.save(shopEntity);
        }
    }

    public static void deleteOrderUser(Long orderUserId) {
        OrderUserEntity userEntity = getUserEntity(orderUserId);
        assert userEntity.getCanceled():"Order not canceled yet.";
        userRepository.delete(userEntity);
    }

    public static void deleteOrderShop(Long orderShopId) {
        OrderShopEntity shopEntity = getShopEntity(orderShopId);
        assert shopEntity.getStatus().equals(OrderStatus.DONE) || shopEntity.getStatus().equals(OrderStatus.REFUNDED):"Order not completed yet.";
        shopRepository.delete(shopEntity);
    }

    public static List<OrderUserEntity> getOrderUser(Long user, Boolean canceled) {
        return userRepository.findByUserAndPaidAndCanceled(user, false, canceled);
    }

    public static List<OrderShopEntity> getOrderUser(Long user, OrderStatus status) {
        List<OrderUserEntity> userEntities = userRepository.findByUser(user);
        List<OrderShopEntity> shopEntities = new ArrayList<>();
        for (OrderUserEntity userEntity: userEntities) {
            shopEntities.addAll(shopRepository.findByOrderUserAndStatus(userEntity.getId(), status));
        }
        return shopEntities;
    }

    public static List<OrderShopEntity> getOrderShop(Long shop, OrderStatus status) {
        return shopRepository.findByShopAndStatus(shop, status);
    }

    public static List<OrderShopEntity> getOrderShop(Long shop, ApplyStatus refundStatus) {
        return shopRepository.findByShopAndRefund(shop, refundStatus);
    }

    public static List<OrderItemEntity> findByOrderShop(Long orderShop) {
        return itemRepository.findByOrderShop(orderShop);
    }

    public static List<OrderShopEntity> findByOrderUser(Long orderUser) {
        return shopRepository.findByOrderUser(orderUser);
    }

    public static OrderUserEntity getUserEntity(Long userEntity) {
        Optional<OrderUserEntity> optional = userRepository.findById(userEntity);
        assert optional.isPresent():"Order user entity not found.";
        return optional.get();
    }

    public static OrderShopEntity getShopEntity(Long shopEntity) {
        Optional<OrderShopEntity> optional = shopRepository.findById(shopEntity);
        assert optional.isPresent():"Order shop entity not found.";
        return optional.get();
    }

}
