package com.segroup2023.lab.service;

import com.segroup2023.lab.database.entity.CartItem;
import com.segroup2023.lab.database.entity.CartItemEntity;
import com.segroup2023.lab.database.repository.CartItemRepository;
import com.segroup2023.lab.utils.PageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private static CartItemRepository cartItemRepository;
    private static final int pageSize = 8;

    @Autowired
    private CartService(CartItemRepository cartItemRepository) {
        CartService.cartItemRepository = cartItemRepository;
    }

    public static void addToCart(Long userId, Long productId, Long count) {
        CartItemEntity cartItemEntity = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if (cartItemEntity == null) {
            cartItemRepository.save(new CartItemEntity(userId, productId, count));
        } else {
            cartItemEntity.increaseCount(count);
            cartItemRepository.save(cartItemEntity);
        }
    }

    public static Page<CartItem> getCartItems(Long userId, int pageNum) {
        List<CartItemEntity> entities = cartItemRepository.findByUserId(userId);
        List<CartItem> items = new ArrayList<>();
        for(CartItemEntity entity:entities) {
            items.add(new CartItem(entity));
        }
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        return PageConverter.listToPage(items, pageRequest);
    }

    public static void removeCartItem(Long userId, Long cartItemId) {
        CartItemEntity item = getEntity(cartItemId);
        if(item == null || !item.getUserId().equals(userId))
            return;
        cartItemRepository.delete(item);
    }

    public static void removeCartItem(Long userId, Long[] cartItemId) {
        for(Long id:cartItemId) {
            CartItemEntity item = getEntity(id);
            if(item == null || !item.getUserId().equals(userId))
                continue;
            cartItemRepository.delete(item);
        }
    }

    public static void updateDeletedProduct(Long productId) {
        List<CartItemEntity> entities = cartItemRepository.findByProductId(productId);
        for(CartItemEntity entity:entities) {
            entity.setValid(false);
            cartItemRepository.save(entity);
        }
    }

    public static CartItemEntity purchase(Long cartItemId) {
        CartItemEntity entity = getEntity(cartItemId);
        assert entity != null : "Cart item entity not exist.";
        cartItemRepository.delete(entity);
        return entity;
    }

    private static CartItemEntity getEntity(Long cartItemId) {
        Optional<CartItemEntity> entity = cartItemRepository.findById(cartItemId);
        return entity.orElse(null);
    }
}
