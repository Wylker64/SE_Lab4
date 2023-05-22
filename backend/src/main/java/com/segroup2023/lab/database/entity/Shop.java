package com.segroup2023.lab.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "shop")
@ResponseBody
@NoArgsConstructor
public class Shop implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private Long owner;
    @Getter @Setter
    private Long account;
    @Getter
    private String name, type, idCard, description, address;
    @Getter
    private Double capital;
    @Getter
    private Date date;
    @Getter @Setter
    private boolean approved;
    @Getter @Setter
    private boolean deleting;

    @ManyToOne
    @JoinColumn(name = "activity")
    private Activity appliedActivity;

    public Shop(User user, String name,
                String type, String idCard,
                String description, String address,
                Double capital, Date date) {
        this.owner = user.getId();
        this.name = name;
        this.type = type;
        this.idCard = idCard;
        this.description = description;
        this.address = address;
        this.capital = capital;
        this.date = date;
        this.approved = false;
        this.deleting = false;
    }

    public Activity getAppliedActivity() {
        return appliedActivity;
    }
    public void setAppliedActivity(Activity appliedActivity) {
        this.appliedActivity = appliedActivity;
    }
}
