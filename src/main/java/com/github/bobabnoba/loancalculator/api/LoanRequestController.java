package com.github.bobabnoba.loancalculator.api;

import com.github.bobabnoba.loancalculator.api.dto.LoanRequestDto;
import com.github.bobabnoba.loancalculator.api.dto.LoanResponseDto;
import com.github.bobabnoba.loancalculator.service.LoanRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/loans")
public class LoanRequestController {
	private final LoanRequestService service;

	public LoanRequestController(LoanRequestService service) {
		this.service = service;
	}

	@PostMapping("/calculate")
	public ResponseEntity<LoanResponseDto> calculate(@Valid @RequestBody LoanRequestDto payload) {
		LoanResponseDto response = service.create(payload);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
