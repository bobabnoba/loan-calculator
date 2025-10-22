package com.github.bobabnoba.loancalculator.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "installments")
public class InstallmentEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_request_id", nullable = false)
    private LoanRequestEntity loanRequest;

    @Column(name = "month_index", nullable = false)
    private int monthIndex;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal interest;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal principal;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal payment;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    private InstallmentEntity() {
    }

    public InstallmentEntity(LoanRequestEntity loanRequest,
                             int monthIndex,
                             BigDecimal interest,
                             BigDecimal principal,
                             BigDecimal payment,
                             BigDecimal balance) {
        this.loanRequest = loanRequest;
        this.monthIndex = monthIndex;
        this.interest = interest;
        this.principal = principal;
        this.payment = payment;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public int getMonthIndex() {
        return monthIndex;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public LoanRequestEntity getLoanRequest() {
        return loanRequest;
    }
}

