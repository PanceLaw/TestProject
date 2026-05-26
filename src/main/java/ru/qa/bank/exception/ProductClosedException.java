package ru.qa.bank.exception;

public class ProductClosedException extends RuntimeException {
    public ProductClosedException(String message) {
        super(message);
    }
}
