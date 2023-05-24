package com.segroup2023.lab.controller;

import com.segroup2023.lab.database.entity.Activity;
import com.segroup2023.lab.database.entity.ProductCategory;
import com.segroup2023.lab.database.entity.Shop;
import com.segroup2023.lab.exception.type.BadRequestException;
import com.segroup2023.lab.exception.type.InsufficientBalanceException;
import com.segroup2023.lab.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @PostMapping
    public Activity createActivity(@RequestBody Activity activity) throws InsufficientBalanceException, BadRequestException {
        return activityService.createActivity(activity);
    }

    @GetMapping("/application")
    public List<Shop> getAllPendingApplication() {
        return activityService.getAllPendingApplication();
    }

    @GetMapping("/{id}")
    public Activity getActivity(@PathVariable Long id) {
        return activityService.getActivity(id);
    }

    @GetMapping("/all")
    public List<Activity> getAllActivities() {
        return activityService.getAllActivities();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity activity) {
        try {
            return ResponseEntity.ok(activityService.updateActivity(id, activity));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Activity> deleteActivity(@PathVariable Long id) {
        try {
            activityService.deleteActivity(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/product_categories")
    public List<ProductCategory> getProductCategories() {
        return activityService.getAllProductCategories();
    }

    @PostMapping("/shops/{shopId}/activities/{activityId}/apply")
    public Shop applyForActivity(@PathVariable Long shopId, @PathVariable Long activityId) {
        return activityService.applyForActivity(shopId, activityId);
    }

    @PostMapping("/shops/{shopId}/approve")
    public Shop approveApplication(@PathVariable Long shopId) {
        return activityService.approveApplication(shopId);
    }


}
