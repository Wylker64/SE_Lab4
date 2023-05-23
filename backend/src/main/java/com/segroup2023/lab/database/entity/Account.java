package com.segroup2023.lab.database.entity;

import com.segroup2023.lab.exception.type.BadRequestException;
import com.segroup2023.lab.exception.type.InsufficientBalanceException;
import com.segroup2023.lab.service.FlowService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
@ResponseBody
@NoArgsConstructor
public class Account implements Serializable {
    public enum Type{
        PERSONAL,PROFIT
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter @Setter
    private Double balance;
    @Getter
    private Long owner;
    @Enumerated(EnumType.STRING)
    @Getter
    private Type type;

    public Account(Long owner, Type type) {
        this.balance = 0.0;
        this.owner = owner;
        this.type = type;
    }

    public void charge(Double amount) throws BadRequestException {
        if(amount <= 0)
            throw new BadRequestException("Charge value less than or equal 0.");
        balance += amount;
    }

    public void transferTo(Account dst, Double amount) throws InsufficientBalanceException {
        if(balance < amount)
            throw new InsufficientBalanceException(balance);
        balance -= amount;
        dst.balance += amount;

        FlowService.addFlow(this,dst, new BigDecimal(amount));
    }
}
