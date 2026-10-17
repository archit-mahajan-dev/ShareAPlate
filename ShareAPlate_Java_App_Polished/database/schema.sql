CREATE DATABASE IF NOT EXISTS shareaplate
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shareaplate;

CREATE TABLE IF NOT EXISTS donations (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  food_name VARCHAR(100) NOT NULL,
  food_type VARCHAR(40) NOT NULL,
  quantity INT NOT NULL,
  available_until DATETIME NOT NULL,
  donor_name VARCHAR(100) NOT NULL,
  contact_details VARCHAR(120) NOT NULL,
  pickup_location VARCHAR(180) NOT NULL,
  notes VARCHAR(500),
  status ENUM('Available','Reserved','Collected') NOT NULL DEFAULT 'Available',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_donations_status (status),
  INDEX idx_donations_type (food_type),
  INDEX idx_donations_name (food_name)
);
