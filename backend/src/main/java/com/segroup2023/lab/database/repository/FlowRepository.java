package com.segroup2023.lab.database.repository;

import java.util.Date;
import java.util.List;

import com.segroup2023.lab.database.entity.Account;
import com.segroup2023.lab.database.entity.FlowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FlowRepository extends JpaRepository<FlowEntity,Long> {
    // Logic to retrieve flow records related to merchants

    @Query("SELECT f FROM FlowEntity f WHERE f.fromAccount = :account OR f.toAccount = :account")
    List<FlowEntity> findByFromAccountOrToAccount(@Param("account") Account account);

    @Query("SELECT f FROM FlowEntity f WHERE (f.fromAccount = :account OR f.toAccount = :account) AND f.date BETWEEN :startDate AND :endDate")
    List<FlowEntity> findByFromAccountOrToAccountAndDateBetween(@Param("account") Account account, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    FlowEntity findByFromAccountAndToAccount(Account fromAccount, Account toAccount);
}
