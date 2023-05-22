package com.segroup2023.lab.database.repository;

import java.time.LocalDate;
import java.util.List;
import com.segroup2023.lab.database.entity.FlowEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FlowRepository extends JpaRepository<FlowEntity,Long> {
    // Logic to retrieve flow records related to merchants

    List<FlowEntity> findByMerchantId(Long merchantId);
    List<FlowEntity> findByMerchantStoreId(Long merchantStoreId);
    List<FlowEntity> findByAdminId(Long adminId);
    List<FlowEntity> findByAdminIntermediaryId(Long adminIntermediaryId);

    List<FlowEntity> findAll();

    FlowEntity findByUserIdAndProductId(Long userId, Long productId);

    Object save(FlowEntity flowEntity);

    FlowEntity findByFromAccountAndToAccount(String fromAccount, String toAccount);

    List<FlowEntity> findByUserId(Long userId, int offset, int pageSize);

    List<FlowEntity> findByUserIdAndDate(Long userId, LocalDate startDate, LocalDate endDate, int offset, int pageSize);
}
