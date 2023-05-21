package com.segroup2023.lab.service;

import com.segroup2023.lab.database.entity.Product;
import com.segroup2023.lab.database.entity.ProductEntity;
import com.segroup2023.lab.database.entity.ProductInfo;
import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.database.repository.ProductInfoRepository;
import com.segroup2023.lab.database.repository.ProductRepository;
import com.segroup2023.lab.utils.FileSaver;
import com.segroup2023.lab.utils.PageConverter;
import com.segroup2023.lab.utils.ApplyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private static ProductRepository productRepository;
    private static ProductInfoRepository infoRepository;
    private static final int pageSize = 8;
    private static final String imageDir = "/product/img";

    @Autowired
    private ProductService(ProductRepository productRepository, ProductInfoRepository infoRepository) {
        ProductService.productRepository = productRepository;
        ProductService.infoRepository = infoRepository;
    }

    public static Page<Product> getProducts(int pageNum, Long shopId, User.Identity access) {
        List<ProductEntity> productEntities;
        if(access.equals(User.Identity.VENDOR)) {
            productEntities = productRepository.findByShopIdAndCreateStatusAndDeleted(shopId, ApplyStatus.APPROVED, false);
            productEntities.addAll(productRepository.findByShopIdAndCreateStatusAndDeleted(shopId, ApplyStatus.WAITING, false));
        } else
            productEntities = productRepository.findByShopIdAndCreateStatusAndDeleted(shopId, ApplyStatus.APPROVED, false);
        List<Product> products = new ArrayList<>();
        for(ProductEntity productEntity : productEntities) {
            products.add(new Product(productEntity));
        }
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        return PageConverter.listToPage(products, pageRequest);
    }

    public static Page<ProductInfo> getRecords(int pageNum, Long shopId) {
        List<ProductEntity> products = productRepository.findByShopId(shopId);
        List<ProductInfo> records = new ArrayList<>();
        for(ProductEntity productEntity : products) {
            records.addAll(infoRepository.findByProductId(productEntity.getId()));
        }
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        return PageConverter.listToPage(records, pageRequest);
    }

    public static void create(Long shopId, String name, String description, Double price, List<MultipartFile> images) {
        ProductEntity productEntity = new ProductEntity(shopId);
        productRepository.save(productEntity);
        ProductInfo info = new ProductInfo(productEntity.getId(), name, description, price, images.size(), ApplyStatus.WAITING, null, true);
        infoRepository.save(info);
        for (int i = 0; i < images.size(); i++) {
            FileSaver.save(imageDir, info.getId() + "-" + i + ".png", images.get(i));
        }
    }

    public static Page<ProductInfo> createReview(int pageNum) {
        List<ProductInfo> infoList = infoRepository.findByCreateStatus(ApplyStatus.WAITING);
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        return PageConverter.listToPage(infoList, pageRequest);
    }

    public static void createApproval(Long productInfoId, Boolean approved) {
        ProductInfo info = getInfo(productInfoId);
        ProductEntity productEntity = getEntity(info.getProductId());
        if(approved) {
            info.setCreateStatus(ApplyStatus.APPROVED);
            productEntity.setCreateStatus(ApplyStatus.APPROVED);
        } else {
            info.setCreateStatus(ApplyStatus.DENIED);
            productEntity.setCreateStatus(ApplyStatus.DENIED);
        }
        infoRepository.save(info);
        productRepository.save(productEntity);
    }

    public static void modify(Long productId, String name, String description, Double price, MultipartFile[] images) {
        ProductInfo info = new ProductInfo(productId, name, description, price, images.length, null, ApplyStatus.WAITING, false);
        infoRepository.save(info);
        for (int i = 0; i < images.length; i++) {
            FileSaver.save(imageDir, info.getId() + "-" + i + ".png", images[i]);
        }
    }

    public static Page<ProductInfo> modifyReview(int pageNum) {
        List<ProductInfo> infoList = infoRepository.findByModifyStatus(ApplyStatus.WAITING);
        Pageable pageRequest = PageRequest.of(pageNum, pageSize);
        return PageConverter.listToPage(infoList, pageRequest);
    }

    public static void modifyApproval(Long infoId, Boolean approved) {
        ProductInfo after = getInfo(infoId);
        ProductInfo before = infoRepository.findByProductIdAndMainTrue(after.getProductId());
        if(approved) {
            after.setModifyStatus(ApplyStatus.APPROVED);
            after.setMain(true);
            before.setMain(false);
        } else {
            after.setModifyStatus(ApplyStatus.DENIED);
        }
        infoRepository.save(before);
        infoRepository.save(after);
    }

    public static void delete(Long productId) {
        ProductEntity entity = getEntity(productId);
        entity.setDeleted(true);
        productRepository.save(entity);
    }

    private static ProductInfo getInfo(Long infoId) {
        Optional<ProductInfo> infoOptional = infoRepository.findById(infoId);
        return infoOptional.orElse(null);
    }

    private static ProductEntity getEntity(Long productId) {
        Optional<ProductEntity> productOptional = productRepository.findById(productId);
        return productOptional.orElse(null);
    }

    public static ProductInfo getMainInfo(Long productId) {
        return infoRepository.findByProductIdAndMainTrue(productId);
    }

    public static Long getShopId(Long productId) {
        ProductEntity entity = getEntity(productId);
        return entity.getShopId();
    }

    public static Product getProduct(Long productId) {
        ProductEntity entity = getEntity(productId);
        return new Product(entity);
    }

    public ProductEntity getProductById(Long productId) {
        Optional<ProductEntity> productOptional = productRepository.findById(productId);
        return productOptional.orElse(null);
    }


}
