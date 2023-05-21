package com.segroup2023.lab.database.entity;

import com.segroup2023.lab.utils.ApplyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "product_info")
@NoArgsConstructor
public class ProductInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private Long productId;
    @Getter @Setter
    private String name, description;
    @Getter @Setter
    private Double price;
    @Getter @Setter
    private Integer image_num;
    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private ApplyStatus createStatus, modifyStatus;
    @Getter @Setter
    private Boolean main;

    public ProductInfo(Long productId, String name, String description, Double price, Integer image_num, ApplyStatus createStatus, ApplyStatus modifyStatus, Boolean main) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image_num = image_num;
        this.createStatus = createStatus;
        this.modifyStatus = modifyStatus;
        this.main = main;
    }

}
