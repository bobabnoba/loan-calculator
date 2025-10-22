package com.github.bobabnoba.loancalculator.persistence;

import com.github.bobabnoba.loancalculator.persistence.entity.LoanRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequestEntity, UUID> {
}
