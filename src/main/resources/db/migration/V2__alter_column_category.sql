ALTER TABLE category
ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP,
ALTER COLUMN updated_at DROP NOT NULL,
ALTER COLUMN updated_at SET DEFAULT NULL;