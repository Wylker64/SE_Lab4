package com.segroup2023.lab.service;

import com.segroup2023.lab.database.entity.Account;
import com.segroup2023.lab.database.entity.Shop;
import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.database.repository.ShopRepository;
import com.segroup2023.lab.exception.type.BadRequestException;
import com.segroup2023.lab.exception.type.FieldConflictException;
import com.segroup2023.lab.exception.type.InsufficientBalanceException;
import com.segroup2023.lab.exception.type.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShopService {
    private static ShopRepository shopRepository;

    private static final int pageSize = 8;

    @Autowired
    private ShopService(ShopRepository shopRepository) {
        ShopService.shopRepository = shopRepository;
    }

    public static void apply(User vendor, Shop shop)
            throws FieldConflictException, InsufficientBalanceException {
        checkConflict(shop);
        AccountService.transferToAdmin(vendor.getId(), shop.getCapital());
        shopRepository.save(shop);
    }

    private static void checkConflict(Shop shop) throws FieldConflictException {
        List<Shop> shops = shopRepository.findByIdCard(shop.getIdCard());
        if(!shops.isEmpty())
            throw new FieldConflictException("Identity card number");
    }

    public static Shop findById(Long id) {
        Optional<Shop> optionalShop = shopRepository.findById(id);
        return optionalShop.orElse(null);
    }

    public static Page<Shop> findAll(int pageNum) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        return shopRepository.findAll(pageRequest);
    }

    public static Page<Shop> findByApproved(int pageNum, boolean approved) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        return shopRepository.findByApproved(pageRequest, approved);
    }

    public static Page<Shop> findByApprovedTrue(int pageNum) {
        return findByApproved(pageNum, true);
    }

    public static Page<Shop> findByApprovedFalse(int pageNum) {
        return findByApproved(pageNum, false);
    }

    public static Page<Shop> findByDeleting(int pageNum, boolean deleting) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        return shopRepository.findByDeleting(pageRequest, deleting);
    }

    public static Page<Shop> findByOwner(int pageNum, Long owner) {
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        return shopRepository.findByOwner(pageRequest, owner);
    }

    public static void approve(User user, Long shopId, boolean approved)
            throws ItemNotFoundException, BadRequestException, InsufficientBalanceException {
        Shop shop = ShopService.findById(shopId);
        if(shop == null)
            throw new BadRequestException("Shop not found.");
        if(shop.isApproved())
            throw new ItemNotFoundException("Unapproved Shop");
        if(!approved) {
            AccountService.undoTransferToAdmin(user.getId(), shop.getCapital());
            shopRepository.delete(shop);
        } else {
            shop.setApproved(true);
            AccountService.createShopAccount(shop);
            shopRepository.save(shop);
            AccountService.adminProfit(shop.getCapital());
        }
    }

    public static boolean belongTo(User user, Shop shop) {
        return shop.getOwner().equals(user.getId());
    }

    public static boolean belongTo(Long userId, Long shopId) {
        Shop shop = findById(shopId);
        if(shop == null)
            return false;
        return shop.getOwner().equals(userId);
    }

    public static void deleteApply(Shop shop) {
        if(!shop.isApproved()) {
            shopRepository.delete(shop);
        } else {
            shop.setDeleting(true);
            shopRepository.save(shop);
        }
    }

    public static void deleteApprove(Shop shop) throws BadRequestException {
        AccountService.deleteShopAccount(shop);
        shopRepository.delete(shop);
    }

    public static Account getAccount(Shop shop) throws BadRequestException {
        if(!shop.isApproved())
            return null;
        return AccountService.findById(shop.getAccount());
    }

    public static void updateSalesInfo(Long shopId, Long volume, Double amount) {
        Shop shop = findById(shopId);
        shop.setSalesVolume(shop.getSalesVolume() + volume);
        shop.setSalesAmount(shop.getSalesAmount() + amount);
        shopRepository.save(shop);
    }

}
