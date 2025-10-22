package com.github.bobabnoba.loancalculator;

import com.github.bobabnoba.loancalculator.persistence.InstallmentRepository;
import com.github.bobabnoba.loancalculator.persistence.LoanRequestRepository;
import com.github.bobabnoba.loancalculator.persistence.entity.InstallmentEntity;
import com.github.bobabnoba.loancalculator.persistence.entity.LoanRequestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class InstallmentRepositoryTest {

    @Autowired
    LoanRequestRepository loanRequestRepository;
    @Autowired
    InstallmentRepository installmentRepository;

    @Test
    void should_return_installments_ordered_by_month_for_given_loan() {
        var parent = loanRequestRepository.save(new LoanRequestEntity(
                new BigDecimal("10000.00"), new BigDecimal("5.0"), 24));
        var row1 = new InstallmentEntity(parent, 1, bd("41.67"), bd("397.04"), bd("438.71"), bd("9602.96"));
        var row2 = new InstallmentEntity(parent, 2, bd("39.18"), bd("399.53"), bd("438.71"), bd("9203.43"));
        installmentRepository.saveAll(List.of(row1, row2));

        var found = installmentRepository.findByLoanRequestIdOrderByMonthIndexAsc(parent.getId());

        assertEquals(2, found.size());
        assertEquals(1, found.getFirst().getMonthIndex());
        assertEquals(parent.getId(), found.getFirst().getLoanRequest().getId());
        assertEquals(bd("438.71"), found.getFirst().getPayment());
    }

    private static BigDecimal bd(String s) { return new BigDecimal(s); }
}