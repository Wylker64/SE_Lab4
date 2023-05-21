package com.segroup2023.lab.database.entity;

import com.segroup2023.lab.utils.ApplyStatus;
import lombok.Getter;

import java.io.Serializable;

public class Product implements Serializable {
    @Getter
    private final Long id, shopId, infoId;
    @Getter
    private final String name, description;
    @Getter
    private final Double price;
    @Getter
    private final Integer image_num;
    @Getter
    private final Boolean approved;
    @Getter
    private final ApplyStatus createStatus, modifyStatus;
    private ProductEntity entity;
    public Product(ProductEntity entity) {
        ProductInfo info = entity.getInfo();
        id = entity.getId();
        shopId = entity.getShopId();
        infoId = info.getId();
        name = info.getName();
        description = info.getDescription();
        price = info.getPrice();
        image_num = info.getImage_num();
        approved = entity.getCreateStatus().equals(ApplyStatus.APPROVED);
        createStatus = entity.getCreateStatus();
        modifyStatus = info.getModifyStatus();
    }

    public void setProduct(ProductEntity entity) {
        this.entity = entity;
    }

    public ProductEntity getProduct() {
        return this.entity;
    }

}
