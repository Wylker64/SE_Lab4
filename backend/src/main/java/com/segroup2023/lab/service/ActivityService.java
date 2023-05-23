package com.segroup2023.lab.service;

import com.segroup2023.lab.database.entity.Activity;
import com.segroup2023.lab.database.entity.ProductCategory;
import com.segroup2023.lab.database.entity.Shop;
import com.segroup2023.lab.database.repository.ActivityRepository;
import com.segroup2023.lab.database.repository.ProductCategoryRepository;
import com.segroup2023.lab.database.repository.ProductRepository;
import com.segroup2023.lab.database.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;


//管理员开启商城补贴活动，需要预设：
//活动持续的时间
//参加活动的商品类别（list）
//⽤于活动的资⾦数量
//活动规则，规则设置为满x减y
//参加商家的注册资⾦、⽉销量、⽉销售额等阈值信息，⾃⾏设置

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




    public Activity createActivity(Activity activity) {

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

    public void deleteActivity(Long id) {
        if(activityRepository.existsById(id))
            activityRepository.deleteById(id);
        else
            throw new IllegalArgumentException("Activity with id " + id  + "not found");
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
}
