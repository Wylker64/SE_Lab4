package com.segroup2023.lab.database.repository;

import com.segroup2023.lab.database.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserAndValidTrue(Long userId);
}
