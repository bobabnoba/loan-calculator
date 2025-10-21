package com.github.bobabnoba.loancalculator.api.mapper;

import com.github.bobabnoba.loancalculator.api.dto.LoanRequestDto;
import com.github.bobabnoba.loancalculator.domain.LoanRequest;
import org.springframework.stereotype.Component;

@Component
public class LoanRequestMapper {
	public LoanRequest toEntity(LoanRequestDto dto) {
		return new LoanRequest(dto.amount(), dto.annualInterestPercent(), dto.termMonths());
	}
}

