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

    private static final int SCALE_CENTS = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_EVEN;
    private static final MathContext MATH_CONTEXT = new MathContext(34, ROUNDING);

    @Override
    public RepaymentSchedule calculate(LoanSpec spec) {
        validate(spec);

        BigDecimal principalAmount = spec.amount();
        BigDecimal annualRatePercent = spec.annualRatePercentage();
        int termMonths = spec.termMonths();

        try {
            BigDecimal monthlyRate = monthlyRateFromAnnualPercent(annualRatePercent);
            BigDecimal monthlyPayment = computeMonthlyPayment(principalAmount, monthlyRate, termMonths);

            List<Installment> installments = buildSchedule(principalAmount, monthlyRate, monthlyPayment, termMonths);
            BigDecimal totalInterest = installments.stream().map(Installment::interest).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(SCALE_CENTS, ROUNDING);

            BigDecimal totalCost = principalAmount.add(totalInterest).setScale(SCALE_CENTS, ROUNDING);

            return new RepaymentSchedule(monthlyPayment, totalInterest, totalCost, List.copyOf(installments));

        } catch (ArithmeticException ae) {
            throw new InvalidLoanSpecException("numeric error during schedule calculation: " + ae.getMessage());
        }
    }

    private static void validate(LoanSpec spec) {
        BigDecimal principal = spec.amount();
        if (principal == null || principal.signum() <= 0) {
            throw new InvalidLoanSpecException("amount must be > 0");
        }

        BigDecimal annualPercent = spec.annualRatePercentage();
        if (annualPercent == null || annualPercent.signum() < 0) {
            throw new InvalidLoanSpecException("annualInterestRate must be >= 0");
        }

        if (spec.termMonths() <= 0) {
            throw new InvalidLoanSpecException("termMonths must be > 0");
        }
    }

    private static BigDecimal monthlyRateFromAnnualPercent(BigDecimal annualPercent) {
        return annualPercent.divide(BigDecimal.valueOf(1200), MATH_CONTEXT);
    }

    private static BigDecimal computeMonthlyPayment(BigDecimal principal, BigDecimal monthlyRate, int termMonths) {
        if (monthlyRate.signum() == 0) {
            return principal.divide(BigDecimal.valueOf(termMonths), SCALE_CENTS, ROUNDING);
        }
        BigDecimal onePlus = BigDecimal.ONE.add(monthlyRate, MATH_CONTEXT);
        BigDecimal denominator = BigDecimal.ONE.subtract(pow(onePlus, -termMonths), MATH_CONTEXT);
        return principal.multiply(monthlyRate, MATH_CONTEXT).divide(denominator, SCALE_CENTS, ROUNDING);
    }

    private static List<Installment> buildSchedule(BigDecimal principal, BigDecimal monthlyRate, BigDecimal monthlyPayment, int termMonths) {
        List<Installment> rows = new ArrayList<>(termMonths);
        BigDecimal remaining = principal;

        for (int month = 1; month <= termMonths; month++) {
            BigDecimal interestPortion = cents(remaining.multiply(monthlyRate, MATH_CONTEXT));
            BigDecimal principalPortion = cents(monthlyPayment.subtract(interestPortion));
            BigDecimal newBalance = remaining.subtract(principalPortion, MATH_CONTEXT);

            if (month == termMonths) {
                BigDecimal residue = cents(newBalance);
                if (residue.compareTo(BigDecimal.ZERO) != 0) {
                    principalPortion = principalPortion.add(residue);
                    newBalance = newBalance.subtract(residue, MATH_CONTEXT);
                }
            }

            BigDecimal totalThisMonth = cents(principalPortion.add(interestPortion));
            rows.add(new Installment(month, interestPortion, principalPortion, totalThisMonth, cents(newBalance)));

            remaining = newBalance;
        }

        return rows;
    }

    private static BigDecimal pow(BigDecimal base, int exponent) {
        if (exponent == 0) return BigDecimal.ONE;
        boolean neg = exponent < 0;
        int e = Math.abs(exponent);

        BigDecimal result = BigDecimal.ONE;
        BigDecimal b = base;

        while (e > 0) {
            if ((e & 1) == 1) result = result.multiply(b, MATH_CONTEXT);
            b = b.multiply(b, MATH_CONTEXT);
            e >>= 1;
        }
        return neg ? BigDecimal.ONE.divide(result, MATH_CONTEXT) : result;
    }

    private static BigDecimal cents(BigDecimal v) {
        return v.setScale(SCALE_CENTS, ROUNDING);
    }
}
