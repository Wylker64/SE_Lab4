package com.segroup2023.lab.exception.type;

import com.segroup2023.lab.database.entity.User;

public class PermissionDeniedException extends Exception{
    private final User.Identity current, needed;
    public PermissionDeniedException(User.Identity current, User.Identity needed) {
        this.current = current;
        this.needed = needed;
    }

    public String getMessage() {
        return "Your are " + current.toString() + ", but Permission " + needed.toString() + " is needed.";
    }
}
