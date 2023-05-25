package com.segroup2023.lab.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "mall_activity")
@NoArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private String name;

    @Getter
    private Date startTime;
    @Getter
    private Date endTime;
    @Getter
    private Double funds;
    @Getter @Setter
    private Double remainingFunds;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "activity_product_category", joinColumns = {@JoinColumn(name = "activity_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "product_category_id", referencedColumnName = "id")})
    @Getter @Setter
    private Set<ProductCategory> productCategories;
    @Column(name = "full_x")
    @Getter
    private Double fullX;
    @Column(name = "minus_y")
    @Getter
    private Double minusY;
    @Getter
    private Double registrationCapitalThreshold;
    @Getter
    private Long monthlySalesVolumeThreshold;
    @Getter
    private Double monthlySalesAmountThreshold;

    public Activity(String name, Date startTime, Date endTime, Double funds, Set<ProductCategory> productCategories,
                    Double fullX, Double minusY, Double registrationCapitalThreshold,
                    Long monthlySalesVolumeThreshold, Double monthlySalesAmountThreshold) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.funds = funds;
        this.productCategories = productCategories;
        this.fullX = fullX;
        this.minusY = minusY;
        this.registrationCapitalThreshold = registrationCapitalThreshold;
        this.monthlySalesVolumeThreshold = monthlySalesVolumeThreshold;
        this.monthlySalesAmountThreshold = monthlySalesAmountThreshold;
        remainingFunds = funds;
    }
    public boolean containsCategory(String category) {
        for (ProductCategory productCategory: productCategories) {
            if (productCategory.getName().equals(category))
                return true;
        }
        return false;
    }

    public void initialize() {
        remainingFunds = funds;
    }

    /**
     * assert enough remaining funds.
     */
    public void decreaseRemainingFunds(Double amount) {
        remainingFunds -= amount;
    }

    public boolean hasSufficientFund() {
        return remainingFunds >= minusY;
    }
}
