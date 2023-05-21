package com.segroup2023.lab.database.repository;

import com.segroup2023.lab.database.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findById(Long id);
    List<Account> findByOwnerAndType(Long owner, Account.Type type);
    Page<Account> findByOwner(Pageable pageable, Long owner);
}
