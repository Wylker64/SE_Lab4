package com.segroup2023.lab.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

@Entity
@Table(name = "address")
@ResponseBody
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter @Setter
    private Long user;
    @Getter @Setter
    private String name, phone, address;
    @Getter @Setter
    private Boolean valid;

    public Address(Long userId, String name, String phone, String address) {
        user = userId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        valid = true;
    }

}
