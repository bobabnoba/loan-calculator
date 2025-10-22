package com.github.bobabnoba.loancalculator.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "loan_requests")
public class LoanRequestEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal amount;

	@Column(name = "annual_interest_percent", nullable = false, precision = 7, scale = 4)
	private BigDecimal annualInterestPercent;

	@Column(name = "term_months", nullable = false)
	private int termMonths;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	private LoanRequestEntity() {
	}

	public LoanRequestEntity(BigDecimal amount, BigDecimal annualInterestPercent, int termMonths) {
		this.amount = amount;
		this.annualInterestPercent = annualInterestPercent;
		this.termMonths = termMonths;
		this.createdAt = Instant.now();
	}

	public UUID getId() {
		return id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public BigDecimal getAnnualInterestPercent() {
		return annualInterestPercent;
	}

	public int getTermMonths() {
		return termMonths;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}