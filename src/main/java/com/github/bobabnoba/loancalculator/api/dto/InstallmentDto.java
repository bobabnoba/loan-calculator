package com.github.bobabnoba.loancalculator.api.dto;

import java.math.BigDecimal;

public record InstallmentDto(int month, BigDecimal interest, BigDecimal principal, BigDecimal total,
							 BigDecimal balance) {
}
