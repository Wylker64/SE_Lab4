package com.segroup2023.lab.service;

import com.segroup2023.lab.database.entity.*;
import com.segroup2023.lab.database.repository.ActivityRepository;
import com.segroup2023.lab.database.repository.ProductCategoryRepository;
import com.segroup2023.lab.database.repository.ProductRepository;
import com.segroup2023.lab.database.repository.ShopRepository;
import com.segroup2023.lab.exception.type.BadRequestException;
import com.segroup2023.lab.exception.type.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@ Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;


    public Activity createActivity(String name, Date startTime, Date endTime, Double funds, List<String> productCategories,
                                   Double fullX, Double minusY, Double registrationCapitalThreshold,
                                   Long monthlySalesVolumeThreshold, Double monthlySalesAmountThreshold)
            throws InsufficientBalanceException {
        AccountService.adminExpend(funds);
        Set<ProductCategory> categories = new HashSet<>();
        for (String product: productCategories) {
            categories.add(productCategoryRepository.findByName(product));
        }
        Activity activity = new Activity(name, startTime, endTime, funds, categories, fullX, minusY, registrationCapitalThreshold,
                monthlySalesVolumeThreshold, monthlySalesAmountThreshold);
        return activityRepository.save(activity);
    }

    public Activity getActivity(Long id) {
        Optional<Activity> optional = activityRepository.findById(id);
        assert optional.isPresent():"Activity not found.";
        return optional.get();
    }

    public void deleteActivity(Long id) throws BadRequestException {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activity with id " + id  + "not found"));

        // Retrieve all shops that are participating in this activity

        List<Shop> shops = shopRepository.findByAppliedActivity(activity);

        // Set the appliedActivity of these shops to null
        for (Shop shop : shops) {
            shop.setAppliedActivity(null);
            shopRepository.save(shop);
        }

        // Transfer remaining funds to profit account
        double remainingFunds = activity.getRemainingFunds();
        Account adminProfitAccount = AccountService.getAdminProfit();
        try {
            AccountService.charge(adminProfitAccount, remainingFunds);
        }
        catch (BadRequestException e) {
            throw new RuntimeException("Failed to transfer remaining funds to profit account", e);
        }

        // Delete the activity
        activityRepository.deleteById(id);
    }


    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepository.findAll();
    }

    public Shop applyForActivity(Long shopId, Long activityId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop with id " + shopId + " not found"));
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activity with id " + activityId + " not found"));

        List<String> shopProductCategories = ProductService.getShopProductCategories(shopId);
        boolean isOverlap = false;
        for (String shopCategory : shopProductCategories) {
            if (activity.containsCategory(shopCategory)) {
                isOverlap = true;
                break;
            }
        }
        if(!isOverlap){
            throw new IllegalArgumentException("Shop with id " + shopId + " does not have any product categories that overlap with activity id " + activityId);
        }
        shop.setAppliedActivity(activity);
        return shopRepository.save(shop);
    }
    public Shop approveApplication(Long shopId)
    {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found with id " + shopId));
        // Check if the shop has applied for an activity
        Activity activity = shop.getAppliedActivity();
        if (activity == null) {
            throw new IllegalArgumentException("Shop with id " + shopId + " has not applied for any activity");
        }
        // Check if the shop has already been approved for an activity
        if (shop.getApprovedActivity() != null) {
            throw new IllegalArgumentException("Shop with id " + shopId + " has already been approved for an activity");
        }

        shop.setApproved(true);
        shop.setApprovedActivity(activity);

        return shopRepository.save(shop);
    }

    public void lockFund(Activity activity, Double amount) {
        activity.decreaseRemainingFunds(amount);
        activityRepository.save(activity);
    }

    public Shop denyApplication(Long shopId)
    {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found with id " + shopId));
        // Check if the shop has applied for an activity
        Activity activity = shop.getAppliedActivity();
        if (activity == null) {
            throw new IllegalArgumentException("Shop with id " + shopId + " has not applied for any activity");
        }

        //Clear the applied activity
        shop.setAppliedActivity(null);

        return shopRepository.save(shop);
    }

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public List<Shop> getPendingActivity()
    {
        return shopRepository.findByAppliedActivityIsNotNullAndApprovedActivityIsNull();
    }


}
