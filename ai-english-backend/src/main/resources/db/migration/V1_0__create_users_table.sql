CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    auth_provider VARCHAR(50) NOT NULL,
    name VARCHAR(255),
    picture_url VARCHAR(512),
    last_login_at TIMESTAMP,
    proficiency_level VARCHAR(50) NOT NULL,
    subscription_status VARCHAR(50) NOT NULL DEFAULT 'FREE',
    is_verified BOOLEAN DEFAULT FALSE
);
