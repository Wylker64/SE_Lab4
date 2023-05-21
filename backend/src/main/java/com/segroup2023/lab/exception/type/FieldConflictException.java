package com.segroup2023.lab.exception.type;

public class FieldConflictException extends Exception{
    private final String field;
    public FieldConflictException(String field){
        this.field = field;
    }
    public String getField(){
        return field;
    }
}
