# Loan Calculator API

A Spring Boot–based REST API that calculates loan repayment schedules and stores them in a relational database.

---

## Overview

This service computes amortized loan schedules based on **loan amount**, **interest rate**, and **term**, returning detailed monthly installments while persisting results for reference.

---

## Endpoint

**POST** `/api/loans/calculate`

### Example Request

```json
{
  "amount": 1000,
  "annualInterestPercent": 5,
  "termMonths": 10
}
```

### Example Response

```json
{
  "id": "dddca309-2897-4d38-9a08-7deda5d74baa",
  "monthlyPayment": 102.31,
  "totalInterest": 23.06,
  "totalCost": 1023.06,
  "schedule": [
    {
      "month": 1,
      "interest": 4.17,
      "principal": 98.14,
      "payment": 102.31,
      "balance": 901.86
    },
    {
      "month": 2,
      "interest": 3.76,
      "principal": 98.55,
      "payment": 102.31,
      "balance": 803.31
    },
    {
      "month": 3,
      "interest": 3.35,
      "principal": 98.96,
      "payment": 102.31,
      "balance": 704.35
    },
    {
      "month": 4,
      "interest": 2.93,
      "principal": 99.38,
      "payment": 102.31,
      "balance": 604.97
    },
    {
      "month": 5,
      "interest": 2.52,
      "principal": 99.79,
      "payment": 102.31,
      "balance": 505.18
    },
    {
      "month": 6,
      "interest": 2.1,
      "principal": 100.21,
      "payment": 102.31,
      "balance": 404.97
    },
    {
      "month": 7,
      "interest": 1.69,
      "principal": 100.62,
      "payment": 102.31,
      "balance": 304.35
    },
    {
      "month": 8,
      "interest": 1.27,
      "principal": 101.04,
      "payment": 102.31,
      "balance": 203.31
    },
    {
      "month": 9,
      "interest": 0.85,
      "principal": 101.46,
      "payment": 102.31,
      "balance": 101.85
    },
    {
      "month": 10,
      "interest": 0.42,
      "principal": 101.85,
      "payment": 102.27,
      "balance": 0.0
    }
  ]
}
```

---

## Tests

Includes:

- **Unit tests** (domain, service)
- **Data JPA tests** (repository)
- **Web layer tests** (controller + validation)

---

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- Flyway
- PostgreSQL / H2 (for tests)
- JUnit 5 + Mockito
