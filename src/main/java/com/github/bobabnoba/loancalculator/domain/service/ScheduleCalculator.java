package com.github.bobabnoba.loancalculator.domain.service;

import com.github.bobabnoba.loancalculator.domain.model.LoanSpec;
import com.github.bobabnoba.loancalculator.domain.model.RepaymentSchedule;

public interface ScheduleCalculator {
    RepaymentSchedule calculate(LoanSpec spec);
}