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

import java.text.DecimalFormat;
import java.util.*;

@Slf4j
@Service
public class OrderService {
    private static OrderItemRepository itemRepository;
    private static OrderShopRepository shopRepository;
    private static OrderUserRepository userRepository;
    private static ActivityService activityService;

    @AllArgsConstructor
    public static class OrderItemRequest {
        @NotNull
        @Getter @Setter
        private Long product_id, count;

        public Product getProduct() {
            return ProductService.getProduct(product_id);
        }
    }

    @Autowired
    private OrderService(OrderItemRepository itemRepository, OrderShopRepository shopRepository,
                         OrderUserRepository userRepository, ActivityService activityService) {
        OrderService.itemRepository = itemRepository;
        OrderService.shopRepository = shopRepository;
        OrderService.userRepository = userRepository;
        OrderService.activityService = activityService;
    }

    public static OrderUser order(Long userId, Long addressId, List<OrderItemRequest> items) {
        User user = UserService.getUser(userId);
        Address address = addressId < 0 ? null : AddressService.getAddress(addressId);
        Date time = new Date();
        List<OrderShop> orderShops = new ArrayList<>();
        double userOrderCost = 0.0;
        double userOrderDiscount = 0.0;
        Set<Long> shops = new HashSet<>();
        List<Long> activityIndex = new ArrayList<>();
        List<Double> totalActivityCost = new ArrayList<>();
        List<Integer> lastItemIndex = new ArrayList<>();
        for (OrderItemRequest item: items) {
            Product product = item.getProduct();
            shops.add(product.getShopId());
            Activity activity = item.getProduct().getShop().getCurrentActivity();
            if (activity != null && activity.containsCategory(product.getCategory())) {
                Double cost = item.getCount() * product.getPrice();
                if (activityIndex.contains(activity.getId())) {
                    int index = activityIndex.indexOf(activity.getId());
                    totalActivityCost.set(index, totalActivityCost.get(index) + cost);
                    lastItemIndex.set(index, items.indexOf(item));
                } else {
                    activityIndex.add(activity.getId());
                    totalActivityCost.add(cost);
                    lastItemIndex.add(items.indexOf(item));
                }
            }
        }
        List<Double> totalDiscount = new ArrayList<>();
        for (int i = 0; i < activityIndex.size(); ++i) {
            Activity activity = activityService.getActivity(activityIndex.get(i));
            if (totalActivityCost.get(i) >= activity.getFullX() && activity.hasSufficientFund()) {
                totalDiscount.add(activity.getFullX());
            } else {
                totalDiscount.add(0.0);
            }
        }
        List<Double> remainingDiscount = new ArrayList<>(totalDiscount);
        for (Long shopId: shops) {
            Shop shop = ShopService.findById(shopId);
            List<OrderItem> orderItems = new ArrayList<>();
            double shopOrderCost = 0.0;
            double shopDiscountCost = 0.0;
            boolean lastActivityShop = false;
            for (OrderItemRequest itemRequest: items) {
                Product product = itemRequest.getProduct();
                if (product.getShopId().equals(shopId)) {
                    double cost = product.getPrice() * itemRequest.getCount();
                    shopOrderCost += cost;
                    orderItems.add(new OrderItem(null, product, itemRequest.getCount(), cost));
                    Activity activity = product.getShop().getCurrentActivity();
                    if (activity != null && activity.containsCategory(product.getCategory())) {
                        shopDiscountCost += cost;
                        if (lastItemIndex.get(activityIndex.indexOf(activity.getId())).equals(items.indexOf(itemRequest))) {
                            lastActivityShop = true;
                        }
                    }
                }
            }
            Double discount = 0.0;
            if (shopDiscountCost != 0.0) {
                Activity activity = shop.getCurrentActivity();
                assert activity != null;
                int index = activityIndex.indexOf(activity.getId());
                if (lastActivityShop) {
                    discount = remainingDiscount.get(index);
                } else {
                    discount = Double.valueOf(new DecimalFormat("#.00").format(
                            totalDiscount.get(index) * shopDiscountCost / totalActivityCost.get(index)));
                }
                remainingDiscount.set(index, remainingDiscount.get(index) - discount);
            }
            userOrderDiscount += discount;
            userOrderCost += shopOrderCost;
            orderShops.add(new OrderShop(null, orderItems, shop, shopOrderCost, discount, user, address, time, OrderStatus.PAY, null));
        }
        return new OrderUser(null, time, userOrderCost, userOrderDiscount, false, false, orderShops);
    }

    public static Long save(OrderUser orderUser, Long userId, Long addressId) {
        OrderUserEntity userEntity = new OrderUserEntity(null, userId, addressId, orderUser.getTime(),
                orderUser.getCost(), orderUser.getDiscount(), orderUser.getPaid(), orderUser.getDeleted());
        userRepository.save(userEntity);
        orderUser.setId(userEntity.getId());
        for (OrderShop orderShop: orderUser.getOrders()) {
            OrderShopEntity shopEntity = new OrderShopEntity(null, userEntity.getId(), orderShop.getShop().getId(),
                    orderShop.getCost(), orderShop.getDiscount(), orderShop.getStatus(), orderShop.getRefund());
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

    public static void pay(Long orderShopId) throws InsufficientBalanceException {
        OrderShopEntity shopEntity = getShopEntity(orderShopId);
        AccountService.transferToAdmin(shopEntity.getUser(), shopEntity.getCost());
        shopEntity.setStatus(OrderStatus.SEND);
        shopRepository.save(shopEntity);
    }

    public static void payUser(Long orderUserId) throws InsufficientBalanceException {
        OrderUserEntity userEntity = getUserEntity(orderUserId);
        AccountService.transferToAdmin(userEntity.getUser(), userEntity.getCost());
        List<OrderShopEntity> shopEntities = findByOrderUser(orderUserId);
        for (OrderShopEntity shopEntity: shopEntities) {
            shopEntity.setStatus(OrderStatus.SEND);
            shopRepository.save(shopEntity);
        }
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
        Long volume = 0L;
        List<OrderItemEntity> itemEntities = itemRepository.findByOrderShop(orderShopId);
        for (OrderItemEntity itemEntity: itemEntities) {
            volume += itemEntity.getCount();
        }
        ShopService.updateSalesInfo(shopEntity.getShop(), volume, cost);
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

    public static void cancel(Long orderShopId) {
        OrderShopEntity shopEntity = getShopEntity(orderShopId);
        assert shopEntity.getStatus().equals(OrderStatus.PAY): "Order already paid.";
        shopEntity.setStatus(OrderStatus.CANCELED);
        shopRepository.save(shopEntity);
    }

    public static void deleteOrderShop(Long orderShopId) {
        OrderShopEntity shopEntity = getShopEntity(orderShopId);
        assert shopEntity.getStatus().equals(OrderStatus.DONE) || shopEntity.getStatus().equals(OrderStatus.REFUNDED):"Order not completed yet.";
        shopRepository.delete(shopEntity);
    }

    public static List<OrderShopEntity> getOrderUser(Long user, OrderStatus status) {
        List<OrderUserEntity> userEntities = userRepository.findByUser(user);
        List<OrderShopEntity> shopEntities = new ArrayList<>();
        for (OrderUserEntity userEntity: userEntities) {
            if (status != null)
                shopEntities.addAll(shopRepository.findByOrderUserAndStatus(userEntity.getId(), status));
            else
                shopEntities.addAll(shopRepository.findByOrderUser(userEntity.getId()));
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
