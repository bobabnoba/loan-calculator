package com.github.bobabnoba.loancalculator.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record LoanRequestDto(
        @NotNull @DecimalMin(value = "0.01", message = "amount must be positive") BigDecimal amount,
        @NotNull @DecimalMin(value = "0", message = "annualInterestPercent cannot be negative") BigDecimal annualInterestPercent,
        @Min(value = 1, message = "termMonths must be at least 1") int termMonths) {
}