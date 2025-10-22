package com.github.bobabnoba.loancalculator.web.dto;

import java.math.BigDecimal;

public record InstallmentDto(int month, BigDecimal interest, BigDecimal principal, BigDecimal payment,
							 BigDecimal balance) {
}
