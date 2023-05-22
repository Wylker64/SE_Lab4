package com.segroup2023.lab.database.repository;

import java.time.LocalDate;
import java.util.List;
import com.segroup2023.lab.database.entity.FlowEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FlowRepository extends JpaRepository<FlowEntity,Long> {
    // Logic to retrieve flow records related to merchants


    FlowEntity findByFromAccountAndToAccount(String fromAccount, String toAccount);
    @Query("SELECT f FROM FlowEntity f WHERE f.userId = :userId AND f.date >= :startDate AND f.date <= :endDate")
    List<FlowEntity> findFlowsByUserIdAndDate(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<FlowEntity> findByUserId(Long userId);
}
