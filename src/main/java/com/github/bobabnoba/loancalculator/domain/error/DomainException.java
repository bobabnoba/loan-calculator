package com.github.bobabnoba.loancalculator.domain.error;

public class DomainException extends RuntimeException {
    public DomainException(String message) { super(message); }
}