package com.github.bobabnoba.loancalculator.api.mapper;

import com.github.bobabnoba.loancalculator.api.dto.InstallmentDto;
import com.github.bobabnoba.loancalculator.api.dto.LoanRequestDto;
import com.github.bobabnoba.loancalculator.domain.Installment;
import com.github.bobabnoba.loancalculator.domain.LoanRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanMapper {

	public LoanRequest toEntity(LoanRequestDto dto) {
		return new LoanRequest(dto.amount(), dto.annualInterestPercent(), dto.termMonths());
	}

	public InstallmentDto toDto(Installment e) {
		return new InstallmentDto(
				e.getSeqNo(),
				e.getInterest(),
				e.getPrincipal(),
				e.getTotal(),
				e.getBalance()
		);
	}

	public List<InstallmentDto> toDtos(List<Installment> entities) {
		return entities.stream().map(this::toDto).toList();
	}
}
