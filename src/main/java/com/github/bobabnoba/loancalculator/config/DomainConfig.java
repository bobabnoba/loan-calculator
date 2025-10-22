package com.github.bobabnoba.loancalculator.config;

import com.github.bobabnoba.loancalculator.domain.service.AmortizationScheduleCalculator;
import com.github.bobabnoba.loancalculator.domain.service.ScheduleCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {
    @Bean
    public ScheduleCalculator scheduleCalculator() {
        return new AmortizationScheduleCalculator();
    }
}