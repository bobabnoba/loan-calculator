package com.github.bobabnoba.loancalculator.repository;

import com.github.bobabnoba.loancalculator.domain.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, UUID> {
}
