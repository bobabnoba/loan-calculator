package com.github.bobabnoba.loancalculator;

import com.github.bobabnoba.loancalculator.application.LoanRequestService;
import com.github.bobabnoba.loancalculator.application.impl.LoanRequestServiceImpl;
import com.github.bobabnoba.loancalculator.domain.model.Installment;
import com.github.bobabnoba.loancalculator.domain.model.RepaymentSchedule;
import com.github.bobabnoba.loancalculator.domain.service.ScheduleCalculator;
import com.github.bobabnoba.loancalculator.persistence.InstallmentRepository;
import com.github.bobabnoba.loancalculator.persistence.LoanRequestRepository;
import com.github.bobabnoba.loancalculator.persistence.entity.LoanRequestEntity;
import com.github.bobabnoba.loancalculator.web.dto.LoanRequestDto;
import com.github.bobabnoba.loancalculator.web.mapper.LoanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanRequestServiceTest {

    @Mock
    LoanRequestRepository loanRequestRepository;
    @Mock
    InstallmentRepository installmentRepository;
    @Mock
    ScheduleCalculator calculator;
    LoanMapper mapper = new LoanMapper();
    LoanRequestService service;

    @BeforeEach
    void setUp() {
        service = new LoanRequestServiceImpl(loanRequestRepository, installmentRepository, mapper, calculator);
    }

    @Test
    void should_persist_loan_and_installments_and_return_response() {
        var dto = new LoanRequestDto(new BigDecimal("10000.00"), new BigDecimal("5.0"), 24);
        var parentId = UUID.randomUUID();

        when(loanRequestRepository.save(any())).thenAnswer(inv -> {
            var e = (LoanRequestEntity) inv.getArgument(0);
            Field id = LoanRequestEntity.class.getDeclaredField("id");
            id.setAccessible(true);
            id.set(e, parentId);
            return e;
        });

        var first = new Installment(1, bd("41.67"), bd("397.04"), bd("438.71"), bd("9602.96"));
        var last = new Installment(24, bd("1.81"), bd("436.90"), bd("438.71"), bd("0.00"));
        var schedule = new RepaymentSchedule(bd("438.71"), bd("529.04"), bd("10529.04"), List.of(first, last));

        when(calculator.calculate(any())).thenReturn(schedule);
        when(installmentRepository.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));

        var resp = service.create(dto);

        assertEquals(parentId, resp.id());
        assertEquals(bd("438.71"), resp.monthlyPayment());
        assertEquals(2, resp.schedule().size());
    }

    private static BigDecimal bd(String s) {
        return new BigDecimal(s);
    }
}