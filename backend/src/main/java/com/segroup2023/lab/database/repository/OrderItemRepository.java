package com.segroup2023.lab.database.repository;

import com.segroup2023.lab.database.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    List<OrderItemEntity> findByOrderShop(Long orderShop);
}
