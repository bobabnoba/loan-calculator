CREATE TABLE installments (
  id UUID PRIMARY KEY,
  loan_request_id UUID NOT NULL REFERENCES loan_requests(id) ON DELETE CASCADE,
  seq_no INT NOT NULL,
  interest DECIMAL(19,2) NOT NULL,
  principal DECIMAL(19,2) NOT NULL,
  total DECIMAL(19,2) NOT NULL,
  balance DECIMAL(19,2) NOT NULL
);