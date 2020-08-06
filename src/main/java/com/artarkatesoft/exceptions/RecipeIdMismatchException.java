package com.artarkatesoft.exceptions;

public class RecipeIdMismatchException extends RuntimeException{
    public RecipeIdMismatchException(String message) {
        super(message);
    }
}
