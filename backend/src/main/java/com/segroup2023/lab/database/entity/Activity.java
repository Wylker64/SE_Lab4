package com.segroup2023.lab.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "mall_activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date startTime;
    private Date endTime;
    private Double funds;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "activity_product_category", joinColumns = {@JoinColumn(name = "activity_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "product_category_id", referencedColumnName = "id")})
    @Getter @Setter
    private Set<ProductCategory> productCategories;
    @Column(name = "full_x")
    private Double fullX;
    @Column(name = "minus_y")
    private Double minusY;
    private Double registrationCapitalThreshold;
    private Double monthlySalesVolumeThreshold;
    private Double monthlySalesAmountThreshold;

    //getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Double getFunds() {
        return funds;
    }

    public void setFunds(Double funds) {
        this.funds = funds;
    }

    public Double getFullX() {
        return fullX;
    }

    public void setFullX(Double fullX) {
        this.fullX = fullX;
    }

    public Double getMinusY() {
        return minusY;
    }

    public void setMinusY(Double minusY) {
        this.minusY = minusY;
    }

    public Double getRegistrationCapitalThreshold() {
        return registrationCapitalThreshold;
    }

    public void setRegistrationCapitalThreshold(Double registrationCapitalThreshold) {
        this.registrationCapitalThreshold = registrationCapitalThreshold;
    }

    public Double getMonthlySalesVolumeThreshold() {
        return monthlySalesVolumeThreshold;
    }

    public void setMonthlySalesVolumeThreshold(Double monthlySalesVolumeThreshold) {
        this.monthlySalesVolumeThreshold = monthlySalesVolumeThreshold;
    }

    public Double getMonthlySalesAmountThreshold() {
        return monthlySalesAmountThreshold;
    }

    public void setMonthlySalesAmountThreshold(Double monthlySalesAmountThreshold) {
        this.monthlySalesAmountThreshold = monthlySalesAmountThreshold;
    }

}
