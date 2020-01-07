package com.learnreactivespring.fluxandmonoplayground;

public class CustomException extends Throwable {

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomException(Throwable p) {
        this.message = p.getMessage();
    }
}
