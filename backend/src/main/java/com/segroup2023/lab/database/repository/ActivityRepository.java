package com.segroup2023.lab.database.repository;

import com.segroup2023.lab.database.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
