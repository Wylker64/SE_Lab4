package com.segroup2023.lab.exception.type;

public class LoginFailureException extends Exception{
    public enum Cause{
        USERNAME("Wrong username!"),
        PASSWORD("Wrong password or username!"),
        AUTHORIZATION("Please login!");
        private final String message;
        Cause(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private final Cause cause;

    public LoginFailureException(Cause cause){
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        return cause.getMessage();
    }
}
