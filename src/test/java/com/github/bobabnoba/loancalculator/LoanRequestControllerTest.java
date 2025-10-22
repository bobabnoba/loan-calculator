package com.github.bobabnoba.loancalculator;

import com.github.bobabnoba.loancalculator.application.LoanRequestService;
import com.github.bobabnoba.loancalculator.web.controller.LoanRequestController;
import com.github.bobabnoba.loancalculator.web.dto.InstallmentDto;
import com.github.bobabnoba.loancalculator.web.dto.LoanRequestDto;
import com.github.bobabnoba.loancalculator.web.dto.LoanResponseDto;
import com.github.bobabnoba.loancalculator.web.error.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoanRequestController.class)
@Import(GlobalExceptionHandler.class)
class LoanRequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private LoanRequestService service;

    @Test
    void should_return_201_with_schedule_when_request_is_valid() throws Exception {
        var resp = new LoanResponseDto(UUID.randomUUID(), new BigDecimal("438.71"), new BigDecimal("529.04"), new BigDecimal("10529.04"), List.of(new InstallmentDto(1, new BigDecimal("41.67"), new BigDecimal("397.04"), new BigDecimal("438.71"), new BigDecimal("9602.96"))));
        when(service.create(ArgumentMatchers.any(LoanRequestDto.class))).thenReturn(resp);

        mvc.perform(post("/api/loans/calculate").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "amount": 10000.00,
                  "annualInterestPercent": 5.0,
                  "termMonths": 24
                }
                """)).andExpect(status().isCreated()).andExpect(jsonPath("$.monthlyPayment").value("438.71")).andExpect(jsonPath("$.totalInterest").value("529.04")).andExpect(jsonPath("$.schedule[0].month").value(1));
    }

    @Test
    void should_return_400_when_request_validation_fails() throws Exception {
        var spec = """
                    {"amount":0,"annualInterestPercent":-1,"termMonths":0}
                """;

        mvc.perform(post("/api/loans/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(spec))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").exists());
    }
}