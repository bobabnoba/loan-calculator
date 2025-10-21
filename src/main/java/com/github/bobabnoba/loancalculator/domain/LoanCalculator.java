package com.github.bobabnoba.loancalculator.domain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class LoanCalculator {

	public record Input(BigDecimal amount, BigDecimal annualInterestPercent, int termMonths) {}
	public record Line(int month, BigDecimal interest, BigDecimal principal, BigDecimal total, BigDecimal balance) {}
	public record Result(BigDecimal monthlyPayment, BigDecimal totalInterest, BigDecimal totalCost, List<Line> schedule) {}

	private static final MathContext MC = new MathContext(34, RoundingMode.HALF_UP);

	public Result calculate(Input in) {
		BigDecimal P = in.amount();
		BigDecimal aprPercent = in.annualInterestPercent();
		int n = in.termMonths();

		BigDecimal r = aprPercent
				.divide(BigDecimal.valueOf(12), MC)
				.divide(BigDecimal.valueOf(100), MC);

		BigDecimal payment;
		if (aprPercent.compareTo(BigDecimal.ZERO) == 0) {
			payment = P.divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_UP);
		} else {
			BigDecimal onePlusR = BigDecimal.ONE.add(r, MC);
			BigDecimal denominator = BigDecimal.ONE.subtract(pow(onePlusR, -n), MC);
			payment = P.multiply(r, MC).divide(denominator, 2, RoundingMode.HALF_UP);
		}

		List<Line> lines = new ArrayList<>(n);
		BigDecimal balance = P;
		BigDecimal totalInterest = BigDecimal.ZERO;

		for (int m = 1; m <= n; m++) {
			BigDecimal interest = balance.multiply(r, MC).setScale(2, RoundingMode.HALF_UP);
			BigDecimal principal = payment.subtract(interest).setScale(2, RoundingMode.HALF_UP);
			BigDecimal newBalance = balance.subtract(principal).setScale(2, RoundingMode.HALF_UP);

			if (m == n && newBalance.compareTo(BigDecimal.ZERO) != 0) {
				principal = principal.add(newBalance);
				newBalance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
			}

			BigDecimal total = principal.add(interest).setScale(2, RoundingMode.HALF_UP);
			lines.add(new Line(m, interest, principal, total, newBalance));

			totalInterest = totalInterest.add(interest);
			balance = newBalance;
		}

		BigDecimal totalCost = P.add(totalInterest).setScale(2, RoundingMode.HALF_UP);
		return new Result(payment, totalInterest.setScale(2, RoundingMode.HALF_UP), totalCost, List.copyOf(lines));
	}

	private static BigDecimal pow(BigDecimal base, int exp) {
		if (exp == 0) return BigDecimal.ONE;
		boolean neg = exp < 0;
		int e = Math.abs(exp);
		BigDecimal result = BigDecimal.ONE;
		for (int i = 0; i < e; i++) {
			result = result.multiply(base, MC);
		}
		return neg ? BigDecimal.ONE.divide(result, MC) : result;
	}
}
