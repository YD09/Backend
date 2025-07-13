-- Insert test user
INSERT INTO users (id, name, email, mobile_number, role, is_active) 
VALUES ('c61a93d1-9281-4875-82e5-e3b0675cfe9e', 'Test User', 'test@example.com', '1234567890', 'USER', true);

-- Insert test wallet
INSERT INTO wallets (
  user_id, balance, available_margin, margin_used, total_invested, total_pnl, total_pnl_percentage
) VALUES (
  'c61a93d1-9281-4875-82e5-e3b0675cfe9e', 100000.0, 100000.0, 0.0, 0.0, 0.0, 0.0
);
