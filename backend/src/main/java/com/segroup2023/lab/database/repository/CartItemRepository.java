package com.segroup2023.lab.database.repository;


import com.segroup2023.lab.database.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long>
{
    List<CartItemEntity> findByUserId(Long userId);

    List<CartItemEntity> findByProductId(Long productId);
    CartItemEntity findByUserIdAndProductId(Long userId, Long productId);
}
