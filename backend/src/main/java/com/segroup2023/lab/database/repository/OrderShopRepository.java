package com.segroup2023.lab.database.repository;

import com.segroup2023.lab.database.entity.OrderShopEntity;
import com.segroup2023.lab.utils.ApplyStatus;
import com.segroup2023.lab.utils.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderShopRepository extends JpaRepository<OrderShopEntity, Long> {
    List<OrderShopEntity> findByOrderUser(Long orderUser);
    List<OrderShopEntity> findByOrderUserAndStatus(Long orderUser, OrderStatus status);
    List<OrderShopEntity> findByShopAndStatus(Long shop, OrderStatus status);
    List<OrderShopEntity> findByShopAndRefund(Long shop, ApplyStatus status);
}
