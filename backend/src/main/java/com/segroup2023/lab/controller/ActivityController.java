package com.segroup2023.lab.controller;

import com.segroup2023.lab.database.entity.Activity;
import com.segroup2023.lab.database.entity.Product;
import com.segroup2023.lab.database.entity.ProductCategory;
import com.segroup2023.lab.database.entity.Shop;
import com.segroup2023.lab.exception.type.BadRequestException;
import com.segroup2023.lab.exception.type.InsufficientBalanceException;
import com.segroup2023.lab.service.ActivityService;
import com.segroup2023.lab.service.ProductService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    private static class CreateRequest {
        public String name;
        public Date startTime;
        public Date endTime;
        public Double funds;
        public List<String> productCategories;
        public Double fullX;
        public Double minusY;
        public Double registrationCapitalThreshold;
        public Long monthlySalesVolumeThreshold;
        public Double monthlySalesAmountThreshold;
    }

    @PostMapping
    public Activity createActivity(@RequestBody CreateRequest request) throws InsufficientBalanceException, BadRequestException {
        return activityService.createActivity(request.name, request.startTime, request.endTime, request.funds, request.productCategories,
                request.fullX, request.minusY, request.registrationCapitalThreshold, request.monthlySalesVolumeThreshold,
                request.monthlySalesAmountThreshold);
    }

    @GetMapping("/pending")
    public List<Shop> getPendingActivity() {
        return activityService.getPendingActivity();
    }

    @GetMapping("/{id}")
    public Activity getActivity(@PathVariable Long id) {
        return activityService.getActivity(id);
    }

    @GetMapping("/all")
    public List<Activity> getAllActivities() {
        return activityService.getAllActivities();
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

    @PostMapping("/shops/{shopId}/deny")
    public Shop denyApplication(@PathVariable Long shopId) {
        return activityService.denyApplication(shopId);
    }

    @GetMapping("/product/{id}")
    public Activity getProductActivity(@PathVariable Long id) {
        return ProductService.getActivity(id);
    }

    @GetMapping("/{id}/product")
    public List<Product> getActivityProducts(@PathVariable Long id) {
        return activityService.getActivityProducts(id);
    }

}
