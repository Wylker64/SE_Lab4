package com.segroup2023.lab.database.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@ Entity
@ Table(name = "flow")
@NoArgsConstructor
public class FlowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_account")
    @Getter
    @Setter
    private Account fromAccount;

    @Getter
    @Setter
    private String srcOwner;

    @ManyToOne
    @JoinColumn(name = "to_account")
    @Getter
    @Setter
    private Account toAccount;

    @Getter
    @Setter
    private String dstOwner;

    @Getter
    @Setter
    private String remark;      //备注信息，包含流水类型等


    @Getter
    @Setter
    private BigDecimal amount;


    @Getter
    @Setter
    private Date date;

    // constructors, getters, setters, and other methods

    public FlowEntity(Account fromAccount,String srcOwner, Account toAccount,String dstOwner, BigDecimal amount, String remark) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.srcOwner = srcOwner;
        this.dstOwner = dstOwner;
        this.date=new Date();
        this.remark = remark;
    }

    public void increaseCount(BigDecimal count) {
        this.amount = this.amount.add(count);
    }
}
