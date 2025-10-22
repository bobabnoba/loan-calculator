package com.github.bobabnoba.loancalculator.domain.model;

import java.math.BigDecimal;

public record Installment(int month, BigDecimal interest, BigDecimal principal, BigDecimal payment,
                          BigDecimal balance) {
}