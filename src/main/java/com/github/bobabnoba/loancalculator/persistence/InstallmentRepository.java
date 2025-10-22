package com.github.bobabnoba.loancalculator.persistence;

import com.github.bobabnoba.loancalculator.persistence.entity.InstallmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InstallmentRepository extends JpaRepository<InstallmentEntity, UUID> {
    List<InstallmentEntity> findByLoanRequestIdOrderByMonthIndexAsc(UUID loanRequestId);
}
