package com.github.bobabnoba.loancalculator.domain.model;

import java.math.BigDecimal;
import java.util.List;

public record RepaymentSchedule(BigDecimal monthlyPayment, BigDecimal totalInterest, BigDecimal totalCost,
                                List<Installment> installments) {
}