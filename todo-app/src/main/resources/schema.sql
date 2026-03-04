CREATE TABLE IF NOT EXISTS tasks (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  title VARCHAR(255) NOT NULL,
  content TEXT,
  name VARCHAR(100),
  start_date DATE,
  end_date DATE,
  created_at TIMESTAMP DEFAULT date_trunc('second', now()),
  updated_at TIMESTAMP DEFAULT date_trunc('second', now())
);

CREATE TABLE IF NOT EXISTS login (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT date_trunc('second', now()),
  updated_at TIMESTAMP DEFAULT date_trunc('second', now())
);
