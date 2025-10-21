package com.github.bobabnoba.loancalculator.repository;

import com.github.bobabnoba.loancalculator.domain.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, UUID> {
}
