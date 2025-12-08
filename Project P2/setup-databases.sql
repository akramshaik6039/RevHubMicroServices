-- RevHub Database Setup Script
-- Run this script in MySQL before starting the services

CREATE DATABASE IF NOT EXISTS revhub_users;
CREATE DATABASE IF NOT EXISTS revhub_posts;
CREATE DATABASE IF NOT EXISTS revhub_follows;

-- Verify databases were created
SHOW DATABASES LIKE 'revhub%';
