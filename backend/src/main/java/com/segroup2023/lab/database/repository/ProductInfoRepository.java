package com.segroup2023.lab.database.repository;

import com.segroup2023.lab.database.entity.ProductInfo;
import com.segroup2023.lab.utils.ApplyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {
    Optional<ProductInfo> findById(Long id);
    ProductInfo findByProductIdAndMainTrue(Long productId);
    List<ProductInfo> findByProductId(Long productId);
    List<ProductInfo> findByCreateStatus(ApplyStatus createStatus);
    List<ProductInfo> findByModifyStatus(ApplyStatus modifyStatus);
}
