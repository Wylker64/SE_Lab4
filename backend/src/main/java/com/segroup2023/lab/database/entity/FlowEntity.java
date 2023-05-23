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

    @Column(name = "from_account")
    private Long fromAccount;

    @Column(name = "to_account")
    private Long toAccount;

    @Column(name = "amount")
    @Getter
    @Setter
    private BigDecimal amount;

    @Getter
    @Setter
    private String remarks;

    @Getter
    @Setter
    private Long userId;

    @Getter
    @Setter
    private Date date;

    // constructors, getters, setters, and other methods

    public FlowEntity(Long fromAccount, Long toAccount, BigDecimal amount, String remarks) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.remarks = remarks;
    }

    public void increaseCount(BigDecimal count) {
        this.amount = this.amount.add(count);
    }
}
