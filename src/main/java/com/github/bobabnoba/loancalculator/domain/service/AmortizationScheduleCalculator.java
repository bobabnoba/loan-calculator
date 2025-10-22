package com.github.bobabnoba.loancalculator.domain.service;

import com.github.bobabnoba.loancalculator.domain.error.InvalidLoanSpecException;
import com.github.bobabnoba.loancalculator.domain.model.Installment;
import com.github.bobabnoba.loancalculator.domain.model.LoanSpec;
import com.github.bobabnoba.loancalculator.domain.model.RepaymentSchedule;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public final class AmortizationScheduleCalculator implements ScheduleCalculator {

    private static final MathContext CALCULATION_CONTEXT = new MathContext(34, RoundingMode.HALF_EVEN);

    @Override
    public RepaymentSchedule calculate(LoanSpec spec) {
        BigDecimal principal = spec.amount();
        if (principal == null || principal.signum() <= 0) throw new InvalidLoanSpecException("amount must be > 0");

        BigDecimal annualInterestRate = spec.annualRatePercentage();
        if (annualInterestRate == null || annualInterestRate.signum() < 0)
            throw new InvalidLoanSpecException("annualInterestRate must be >= 0");

        int numberOfMonths = spec.termMonths();
        if (numberOfMonths <= 0) throw new InvalidLoanSpecException("termMonths must be > 0");
        try {
            BigDecimal monthlyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(1200), CALCULATION_CONTEXT);

            BigDecimal monthlyPayment;
            if (annualInterestRate.signum() == 0) {
                monthlyPayment = principal.divide(BigDecimal.valueOf(numberOfMonths), 2, RoundingMode.HALF_EVEN);
            } else {
                BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyInterestRate, CALCULATION_CONTEXT);
                BigDecimal discountFactor = BigDecimal.ONE.subtract(pow(onePlusRate, -numberOfMonths), CALCULATION_CONTEXT);
                monthlyPayment = principal.multiply(monthlyInterestRate, CALCULATION_CONTEXT).divide(discountFactor, 2, RoundingMode.HALF_EVEN);
            }

            List<Installment> installments = new ArrayList<>(numberOfMonths);
            BigDecimal remainingBalance = principal;
            BigDecimal totalInterestPaid = BigDecimal.ZERO;

            for (int month = 1; month <= numberOfMonths; month++) {
                BigDecimal interestPayment = remainingBalance.multiply(monthlyInterestRate, CALCULATION_CONTEXT).setScale(2, RoundingMode.HALF_EVEN);

                BigDecimal principalPayment = monthlyPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_EVEN);

                BigDecimal newBalance = remainingBalance.subtract(principalPayment, CALCULATION_CONTEXT);

                if (month == numberOfMonths) {
                    BigDecimal roundingDifference = newBalance.setScale(2, RoundingMode.HALF_EVEN);
                    if (roundingDifference.compareTo(BigDecimal.ZERO) != 0) {
                        principalPayment = principalPayment.add(roundingDifference);
                        newBalance = newBalance.subtract(roundingDifference, CALCULATION_CONTEXT);
                    }
                }

                BigDecimal totalMonthlyPayment = principalPayment.add(interestPayment).setScale(2, RoundingMode.HALF_EVEN);

                installments.add(new Installment(month, interestPayment, principalPayment, totalMonthlyPayment, newBalance.setScale(2, RoundingMode.HALF_EVEN)));

                totalInterestPaid = totalInterestPaid.add(interestPayment);
                remainingBalance = newBalance;
            }

            BigDecimal totalCost = principal.add(totalInterestPaid).setScale(2, RoundingMode.HALF_EVEN);

            return new RepaymentSchedule(monthlyPayment, totalInterestPaid.setScale(2, RoundingMode.HALF_EVEN), totalCost, List.copyOf(installments));
        } catch (ArithmeticException ae) {
            throw new InvalidLoanSpecException("Numeric error during schedule calculation: " + ae.getMessage());
        }
    }

    private static BigDecimal pow(BigDecimal base, int exponent) {
        if (exponent == 0) return BigDecimal.ONE;
        boolean negative = exponent < 0;
        int power = Math.abs(exponent);

        BigDecimal result = BigDecimal.ONE;
        BigDecimal currentBase = base;

        while (power > 0) {
            if ((power & 1) == 1) result = result.multiply(currentBase, CALCULATION_CONTEXT);
            currentBase = currentBase.multiply(currentBase, CALCULATION_CONTEXT);
            power >>= 1;
        }

        return negative ? BigDecimal.ONE.divide(result, CALCULATION_CONTEXT) : result;
    }

}
