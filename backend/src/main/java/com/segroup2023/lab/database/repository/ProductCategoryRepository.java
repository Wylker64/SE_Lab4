package com.segroup2023.lab.database.repository;


import com.segroup2023.lab.database.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
