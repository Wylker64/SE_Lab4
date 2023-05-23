package com.segroup2023.lab.database.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;



@ Entity
@ Table(name = "flow")
public class FlowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_account")
    @Getter
    @Setter
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account")
    @Getter
    @Setter
    private Account toAccount;


    @Column(name = "amount")
    @Getter
    @Setter
    private BigDecimal amount;


    @Getter
    @Setter
    private Long userId;

    @Getter
    @Setter
    private Date date;

    // constructors, getters, setters, and other methods

    public FlowEntity(Account fromAccount, Account toAccount, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public void increaseCount(BigDecimal count) {
        this.amount = this.amount.add(count);
    }
}
