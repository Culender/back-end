package com.example.back_end.util.exception;

public class EntityDuplicatedException extends RuntimeException{
    public EntityDuplicatedException(String message) {
        super(message);
    }
}
