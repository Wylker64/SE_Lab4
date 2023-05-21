package com.segroup2023.lab.exception.type;

public class InsufficientBalanceException extends Exception {
    private final Double balance;
    public InsufficientBalanceException(Double balance) {
        this.balance = balance;
    }

    public String getMessage() {
        return balance.toString();
    }
}
