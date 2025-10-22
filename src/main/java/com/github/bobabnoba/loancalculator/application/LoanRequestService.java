package com.github.bobabnoba.loancalculator.application;

import com.github.bobabnoba.loancalculator.web.dto.LoanRequestDto;
import com.github.bobabnoba.loancalculator.web.dto.LoanResponseDto;

public interface LoanRequestService {
	LoanResponseDto create(LoanRequestDto dto);

}
