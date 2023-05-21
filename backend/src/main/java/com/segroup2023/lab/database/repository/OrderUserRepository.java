package com.segroup2023.lab.database.repository;

import com.segroup2023.lab.database.entity.OrderUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderUserRepository extends JpaRepository<OrderUserEntity, Long> {
    List<OrderUserEntity> findByUser(Long user);
    List<OrderUserEntity> findByUserAndPaidAndCanceled(Long user, Boolean paid, Boolean canceled);
}
