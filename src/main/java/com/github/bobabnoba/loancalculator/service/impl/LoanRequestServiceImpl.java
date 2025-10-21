package com.github.bobabnoba.loancalculator.service.impl;

import com.github.bobabnoba.loancalculator.api.dto.LoanRequestDto;
import com.github.bobabnoba.loancalculator.api.mapper.LoanRequestMapper;
import com.github.bobabnoba.loancalculator.domain.LoanRequest;
import com.github.bobabnoba.loancalculator.repository.LoanRequestRepository;
import com.github.bobabnoba.loancalculator.service.LoanRequestService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LoanRequestServiceImpl implements LoanRequestService {
	private final LoanRequestRepository repo;
	private final LoanRequestMapper mapper;

	public LoanRequestServiceImpl(LoanRequestRepository repo, LoanRequestMapper mapper) {
		this.repo = repo;
		this.mapper = mapper;
	}

	@Transactional
	@Override
	public void create(LoanRequestDto dto) {
		LoanRequest entity = mapper.toEntity(dto);
		repo.save(entity);
	}
}
