package com.segroup2023.lab.exception.type;

public class ItemNotFoundException extends Exception{
    private final String itemName;
    public ItemNotFoundException(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String getMessage() {
        return itemName + " Not Found!";
    }
}
