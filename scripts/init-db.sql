-- Create databases
CREATE DATABASE IF NOT EXISTS keynote_db;
CREATE DATABASE IF NOT EXISTS conference_db;
CREATE DATABASE IF NOT EXISTS auth_db;

-- Use keynote_db
USE keynote_db;

-- Keynote table for query side
CREATE TABLE IF NOT EXISTS keynotes (
    id VARCHAR(255) PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    fonction VARCHAR(255) NOT NULL
);

-- Use conference_db
USE conference_db;

-- Conference table for query side
CREATE TABLE IF NOT EXISTS conferences (
    id VARCHAR(255) PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    date DATE NOT NULL,
    duree INT NOT NULL,
    nombre_inscrits INT DEFAULT 0,
    score DOUBLE DEFAULT 0.0
);

-- Review table for query side
CREATE TABLE IF NOT EXISTS reviews (
    id VARCHAR(255) PRIMARY KEY,
    conference_id VARCHAR(255) NOT NULL,
    date DATETIME NOT NULL,
    texte TEXT NOT NULL,
    note INT NOT NULL CHECK (note >= 1 AND note <= 5),
    FOREIGN KEY (conference_id) REFERENCES conferences(id) ON DELETE CASCADE
);

-- Use auth_db
USE auth_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    enabled BOOLEAN DEFAULT TRUE
);

-- Roles table
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- User roles junction table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Insert default roles
INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN') ON DUPLICATE KEY UPDATE name=name;

-- Insert default admin user (password: admin123, bcrypt encoded)
INSERT INTO users (username, password, email, enabled) 
VALUES ('admin', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'admin@conference.com', TRUE)
ON DUPLICATE KEY UPDATE username=username;

-- Assign admin role to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
ON DUPLICATE KEY UPDATE user_id=user_id;
