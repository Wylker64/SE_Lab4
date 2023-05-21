package com.segroup2023.lab.database.repository;

import com.segroup2023.lab.database.entity.ProductCategory;
import com.segroup2023.lab.database.entity.ProductEntity;
import com.segroup2023.lab.utils.ApplyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findById(Long id);
    List<ProductEntity> findByShopId(Long shopId);
    List<ProductEntity> findByShopIdAndDeleted(Long shopId, Boolean deleted);
    List<ProductEntity> findByShopIdAndCreateStatusAndDeleted(Long shopId, ApplyStatus create, Boolean deleted);
}
