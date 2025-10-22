package com.github.bobabnoba.loancalculator.web.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record LoanResponseDto(
		UUID id, BigDecimal monthlyPayment, BigDecimal totalInterest, BigDecimal totalCost, List<InstallmentDto> schedule
) {}
