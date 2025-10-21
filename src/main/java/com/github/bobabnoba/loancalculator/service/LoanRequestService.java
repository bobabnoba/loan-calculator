package com.github.bobabnoba.loancalculator.service;

import com.github.bobabnoba.loancalculator.api.dto.LoanRequestDto;
import com.github.bobabnoba.loancalculator.api.dto.LoanResponseDto;

public interface LoanRequestService {
	LoanResponseDto create(LoanRequestDto dto);

}
