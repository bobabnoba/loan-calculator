package com.github.bobabnoba.loancalculator.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "installments")
public class Installment {

	@Id
	@GeneratedValue
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "loan_request_id", nullable = false)
	private LoanRequest loanRequest;

	@Column(name = "seq_no", nullable = false)
	private int seqNo;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal interest;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal principal;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal total;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal balance;

	private Installment() {
	}

	public Installment(LoanRequest loanRequest, int seqNo, BigDecimal interest, BigDecimal principal, BigDecimal total, BigDecimal balance) {
		this.loanRequest = loanRequest;
		this.seqNo = seqNo;
		this.interest = interest;
		this.principal = principal;
		this.total = total;
		this.balance = balance;
	}

	public UUID getId() {
		return id;
	}

	public LoanRequest getLoanRequest() {
		return loanRequest;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public BigDecimal getPrincipal() {
		return principal;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public BigDecimal getBalance() {
		return balance;
	}
}
