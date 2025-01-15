package org.example.exceptions;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(String msg) {
        super(msg);
    }
}
