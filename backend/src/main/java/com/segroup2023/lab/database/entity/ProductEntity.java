package com.segroup2023.lab.database.entity;

import com.segroup2023.lab.service.ProductService;
import com.segroup2023.lab.utils.ApplyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "product")
@NoArgsConstructor
public class ProductEntity implements Serializable {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter @Setter
    private Long shopId;
    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private ApplyStatus createStatus;
    @Getter @Setter
    private Boolean deleted;

    public ProductEntity(Long shopId) {
        this.shopId = shopId;
        this.createStatus = ApplyStatus.WAITING;
        this.deleted = false;
    }

    public ProductInfo getInfo() {
        return ProductService.getMainInfo(id);
    }

}
