package com.segroup2023.lab.database.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;



@ Entity
@ Table(name = "flow")
public class FlowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_account")
    private String fromAccount;

    @Column(name = "to_account")
    private String toAccount;

    @Column(name = "amount")
    @Getter
    @Setter

    private BigDecimal amount;

    @ Getter
    @ Setter
    private String remarks;

    // constructors, getters, setters, and other methods

    public FlowEntity(String fromAccount, String toAccount, BigDecimal amount, String remarks) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.remarks = remarks;
    }

    public void increaseCount(BigDecimal count) {
        this.amount = this.amount.add(count);
    }
}
