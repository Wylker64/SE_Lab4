package com.segroup2023.lab.database.entity;

import com.segroup2023.lab.utils.ApplyStatus;
import com.segroup2023.lab.utils.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_shop")
@NoArgsConstructor
@AllArgsConstructor
public class OrderShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter @Setter
    private Long orderUser, shop;
    @Getter @Setter
    private Double cost;
    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private ApplyStatus refund;

}
