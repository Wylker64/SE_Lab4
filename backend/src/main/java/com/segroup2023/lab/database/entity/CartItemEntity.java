package com.segroup2023.lab.database.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_item")
@NoArgsConstructor
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private Long userId;
    @Getter
    private Long productId;
    @Getter @Setter
    private Long count;
    @Getter @Setter
    private Boolean valid;

    public CartItemEntity(Long userId, Long productId, Long count) {
        this.userId = userId;
        this.productId = productId;
        this.count = count;
        valid = true;
    }

    public void increaseCount(Long count) {
        this.count += count;
    }
}
