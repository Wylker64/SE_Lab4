package com.segroup2023.lab.database.repository;

import com.segroup2023.lab.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    List<User> findByUsername(String username);
    List<User> findByPhone(String phone);
    List<User> findByIdCard(String idCard);
    List<User> findByEmail(String email);
    List<User> findByIdentity(User.Identity identity);
}