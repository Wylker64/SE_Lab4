package com.segroup2023.lab.service;

import com.segroup2023.lab.database.entity.Account;
import com.segroup2023.lab.database.entity.Activity;
import com.segroup2023.lab.database.entity.ProductCategory;
import com.segroup2023.lab.database.entity.Shop;
import com.segroup2023.lab.database.repository.ActivityRepository;
import com.segroup2023.lab.database.repository.ProductCategoryRepository;
import com.segroup2023.lab.database.repository.ProductRepository;
import com.segroup2023.lab.database.repository.ShopRepository;
import com.segroup2023.lab.exception.type.BadRequestException;
import com.segroup2023.lab.exception.type.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;



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




    public Activity createActivity(Activity activity) throws BadRequestException, InsufficientBalanceException {
        Account adminAccount=AccountService.getAdminPersonal();
        Double funds = activity.getFunds();
        if (funds > adminAccount.getBalance()) {
            throw new InsufficientBalanceException(adminAccount.getBalance());
        }


        return activityRepository.save(activity);
    }

    public Activity getActivity(Long id) {
        Optional<Activity> optional = activityRepository.findById(id);
        assert optional.isPresent():"Activity not found.";
        return optional.get();
    }

    public Activity updateActivity(Long id,Activity updatedActivity) {
        return activityRepository.findById(id)
                .map(activity -> {
                    activity.setStartTime(updatedActivity.getStartTime());
                    activity.setEndTime(updatedActivity.getEndTime());
                    activity.setFunds(updatedActivity.getFunds());
                    activity.setProductCategories(updatedActivity.getProductCategories());
                    activity.setFullX(updatedActivity.getFullX());
                    activity.setMinusY(updatedActivity.getMinusY());
                    activity.setRegistrationCapitalThreshold(updatedActivity.getRegistrationCapitalThreshold());
                    activity.setMonthlySalesVolumeThreshold(updatedActivity.getMonthlySalesVolumeThreshold());
                    activity.setMonthlySalesAmountThreshold(updatedActivity.getMonthlySalesAmountThreshold());
                    return activityRepository.save(activity);
                })
                .orElseThrow(() -> new IllegalArgumentException("Activity not found with id " + id));

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

        shop.setAppliedActivity(activity);
        return shopRepository.save(shop);
    }
    public Shop approveApplication(Long shopId)
    {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found with id " + shopId));
        shop.setApproved(true);

        return shopRepository.save(shop);
    }

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public List<Shop> getAllPendingApplication() {
        return shopRepository.findByAppliedActivityIsNotNullAndApprovedActivityIsNull();
    }
}
