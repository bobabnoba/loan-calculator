package com.github.bobabnoba.loancalculator.service.impl;

import com.github.bobabnoba.loancalculator.api.dto.LoanRequestDto;
import com.github.bobabnoba.loancalculator.api.dto.LoanResponseDto;
import com.github.bobabnoba.loancalculator.api.mapper.LoanMapper;
import com.github.bobabnoba.loancalculator.domain.Installment;
import com.github.bobabnoba.loancalculator.domain.LoanCalculator;
import com.github.bobabnoba.loancalculator.domain.LoanRequest;
import com.github.bobabnoba.loancalculator.repository.InstallmentRepository;
import com.github.bobabnoba.loancalculator.repository.LoanRequestRepository;
import com.github.bobabnoba.loancalculator.service.LoanRequestService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LoanRequestServiceImpl implements LoanRequestService {
	private final LoanRequestRepository loanRequestRepository;
	private final InstallmentRepository installmentRepository;
	private final LoanMapper mapper;
	private final LoanCalculator loanCalculator;

	public LoanRequestServiceImpl(LoanRequestRepository repo, InstallmentRepository installmentRepository, LoanMapper mapper, LoanCalculator loanCalculator) {
		this.loanRequestRepository = repo;
		this.installmentRepository = installmentRepository;
		this.mapper = mapper;
		this.loanCalculator = loanCalculator;
	}

	@Transactional
	@Override
	public LoanResponseDto create(LoanRequestDto dto) {
		LoanRequest req = loanRequestRepository.save(mapper.toEntity(dto));

		var res = loanCalculator.calculate(new LoanCalculator.Input(
				dto.amount(), dto.annualInterestPercent(), dto.termMonths()
		));

		var children = res.schedule().stream()
				.map(l -> new Installment(
						req,
						l.month(),
						l.interest(),
						l.principal(),
						l.total(),
						l.balance()
				))
				.toList();
		installmentRepository.saveAll(children);

		var scheduleDtos = mapper.toDtos(children);
		return new LoanResponseDto(req.getId(), res.monthlyPayment(), res.totalInterest(), res.totalCost(), scheduleDtos);
	}

}
