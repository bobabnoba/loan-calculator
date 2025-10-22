package com.github.bobabnoba.loancalculator.domain.model;

import java.math.BigDecimal;

public record LoanSpec(BigDecimal amount, BigDecimal annualRatePercentage, int termMonths) {
}
