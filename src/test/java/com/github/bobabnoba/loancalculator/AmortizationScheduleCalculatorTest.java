package com.github.bobabnoba.loancalculator;

import com.github.bobabnoba.loancalculator.domain.error.InvalidLoanSpecException;
import com.github.bobabnoba.loancalculator.domain.model.LoanSpec;
import com.github.bobabnoba.loancalculator.domain.service.AmortizationScheduleCalculator;
import com.github.bobabnoba.loancalculator.domain.service.ScheduleCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AmortizationScheduleCalculatorTest {

    ScheduleCalculator calc = new AmortizationScheduleCalculator();

    @Test
    void should_calculate_correct_schedule_for_24_month_loan() {
        var spec = new LoanSpec(new BigDecimal("10000.00"), new BigDecimal("5.0"), 24);

        var schedule = calc.calculate(spec);

        assertEquals(new BigDecimal("438.71"), schedule.monthlyPayment());
        assertEquals(new BigDecimal("529.14"), schedule.totalInterest());
        assertEquals(24, schedule.installments().size());
        assertEquals(new BigDecimal("0.00"), schedule.installments().getLast().balance());
    }

    @Test
    void should_calculate_equal_payments_when_interest_is_zero() {
        var spec = new LoanSpec(new BigDecimal("1200.00"), new BigDecimal("0.0"), 12);

        var s = calc.calculate(spec);

        assertEquals(new BigDecimal("100.00"), s.monthlyPayment());
        assertEquals(new BigDecimal("0.00"), s.totalInterest());
        assertEquals(new BigDecimal("0.00"), s.installments().getLast().balance());
    }

    @Test
    void should_throw_invalid_loan_spec_when_amount_is_non_positive() {
        var spec = new LoanSpec(new BigDecimal("0.00"), new BigDecimal("5.0"), 12);

        assertThrows(InvalidLoanSpecException.class, () -> calc.calculate(spec));
    }
}