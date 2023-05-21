package com.segroup2023.lab.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "order_user")
@NoArgsConstructor
@AllArgsConstructor
public class OrderUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter @Setter
    private Long user, address;
    @Getter @Setter
    private Date time;
    @Getter @Setter
    private Double cost;
    @Getter @Setter
    private Boolean paid, canceled;

}
