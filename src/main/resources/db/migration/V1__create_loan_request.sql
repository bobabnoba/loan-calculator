CREATE TABLE IF NOT EXISTS loan_requests (
  id UUID PRIMARY KEY,
  amount DECIMAL(19,2) NOT NULL,
  annual_interest_percent DECIMAL(7,4) NOT NULL,
  term_months INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),

  CONSTRAINT ck_loan_request_amount_pos CHECK (amount > 0),
  CONSTRAINT ck_loan_request_rate_non_neg CHECK (annual_interest_percent >= 0),
  CONSTRAINT ck_loan_request_term_pos CHECK (term_months >= 1)
);
