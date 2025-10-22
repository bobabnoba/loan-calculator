package com.github.bobabnoba.loancalculator.application.impl;

import com.github.bobabnoba.loancalculator.domain.model.RepaymentSchedule;
import com.github.bobabnoba.loancalculator.web.dto.InstallmentDto;
import com.github.bobabnoba.loancalculator.web.dto.LoanRequestDto;
import com.github.bobabnoba.loancalculator.web.dto.LoanResponseDto;
import com.github.bobabnoba.loancalculator.web.mapper.LoanMapper;
import com.github.bobabnoba.loancalculator.domain.model.LoanSpec;
import com.github.bobabnoba.loancalculator.domain.service.ScheduleCalculator;
import com.github.bobabnoba.loancalculator.persistence.InstallmentRepository;
import com.github.bobabnoba.loancalculator.persistence.LoanRequestRepository;
import com.github.bobabnoba.loancalculator.persistence.entity.InstallmentEntity;
import com.github.bobabnoba.loancalculator.persistence.entity.LoanRequestEntity;
import com.github.bobabnoba.loancalculator.application.LoanRequestService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanRequestServiceImpl implements LoanRequestService {
    private final LoanRequestRepository loanRequestRepository;
    private final InstallmentRepository installmentRepository;
    private final LoanMapper mapper;
    private final ScheduleCalculator calculator;

    public LoanRequestServiceImpl(LoanRequestRepository repo, InstallmentRepository installmentRepository, LoanMapper mapper, ScheduleCalculator calculator) {
        this.loanRequestRepository = repo;
        this.installmentRepository = installmentRepository;
        this.mapper = mapper;
        this.calculator = calculator;
    }

    @Transactional
    @Override
    public LoanResponseDto create(LoanRequestDto dto) {
        LoanRequestEntity request = loanRequestRepository.save(mapper.toLoanRequestEntity(dto));

        LoanSpec spec = new LoanSpec(dto.amount(), dto.annualInterestPercent(), dto.termMonths());
        RepaymentSchedule schedule = calculator.calculate(spec);

        List<InstallmentEntity> rows = mapper.toInstallmentEntities(request, schedule.installments());
        installmentRepository.saveAll(rows);

        List<InstallmentDto> installmentDtos = mapper.toInstallmentDtos(rows);
        return new LoanResponseDto(request.getId(), schedule.monthlyPayment(), schedule.totalInterest(), schedule.totalCost(), installmentDtos);
    }
}
