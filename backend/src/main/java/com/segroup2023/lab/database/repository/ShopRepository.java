package com.segroup2023.lab.database.repository;

import com.segroup2023.lab.database.entity.Activity;
import com.segroup2023.lab.database.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findById(Long id);
    List<Shop> findByIdCard(String idCard);
    Page<Shop> findByApproved(Pageable pageable, boolean approved);
    Page<Shop> findByDeleting(Pageable pageable, boolean deleting);
    Page<Shop> findByOwner(Pageable pageable, Long owner);

    List<Shop> findByAppliedActivity(Activity activity);
}
