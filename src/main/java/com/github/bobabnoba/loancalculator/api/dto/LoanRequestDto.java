package com.github.bobabnoba.loancalculator.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record LoanRequestDto(
		@NotNull @DecimalMin("0.01") BigDecimal amount,
		@NotNull @DecimalMin("0") BigDecimal annualInterestPercent,
		@Min(1) int termMonths
) {}