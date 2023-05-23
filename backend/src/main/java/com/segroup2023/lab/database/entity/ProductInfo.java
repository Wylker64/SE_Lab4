package com.segroup2023.lab.database.entity;

import com.segroup2023.lab.utils.ApplyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "product_info")
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private Long productId;
    @Getter @Setter
    private String name, description, category;
    @Getter @Setter
    private Double price;
    @Getter @Setter
    private Integer image_num;
    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private ApplyStatus createStatus, modifyStatus;
    @Getter @Setter
    private Boolean main;

}
