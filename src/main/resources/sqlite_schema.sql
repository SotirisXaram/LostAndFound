-- SQLite schema for Lost and Found application
-- Converted from PostgreSQL schema

-- Users table
CREATE TABLE IF NOT EXISTS users (
    am INTEGER PRIMARY KEY,
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    date_of_birth DATE,
    role VARCHAR(15) NOT NULL CHECK (role IN ('user', 'admin', 'sx')),
    password VARCHAR(255) NOT NULL
);

-- Records table
CREATE TABLE IF NOT EXISTS records (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    uid TEXT NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    record_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    officer_id INTEGER NOT NULL,
    founder_last_name VARCHAR(255) NOT NULL,
    founder_first_name VARCHAR(255) NOT NULL,
    founder_id_number VARCHAR(50) NOT NULL,
    founder_telephone VARCHAR(20),
    founder_street_address VARCHAR(255),
    founder_street_number VARCHAR(20),
    founder_father_name VARCHAR(255),
    founder_area_inhabitant VARCHAR(255),
    found_date DATE NOT NULL,
    found_time TIME NOT NULL,
    found_location VARCHAR(150),
    item_description TEXT NOT NULL,
    item_category VARCHAR(100),
    item_brand VARCHAR(100),
    item_model VARCHAR(100),
    item_color VARCHAR(50),
    item_serial_number VARCHAR(100),
    item_other_details TEXT,
    storage_location VARCHAR(100),
    picture_path VARCHAR(500),
    status VARCHAR(20) DEFAULT 'stored' CHECK (status IN ('stored', 'returned', 'disposed')),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (officer_id) REFERENCES users(am)
);

-- Returns table
CREATE TABLE IF NOT EXISTS returns (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    uid TEXT NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    return_officer INTEGER NOT NULL,
    return_last_name VARCHAR(255) NOT NULL,
    return_first_name VARCHAR(255) NOT NULL,
    return_date DATE NOT NULL,
    return_time TIME,
    return_telephone VARCHAR(20),
    return_id_number VARCHAR(50),
    return_father_name VARCHAR(255),
    return_date_of_birth DATE,
    return_street_address VARCHAR(255),
    return_street_number VARCHAR(20),
    return_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    comment TEXT,
    record_id INTEGER,
    FOREIGN KEY (return_officer) REFERENCES users(am),
    FOREIGN KEY (record_id) REFERENCES records(id)
);

-- Insert default admin user (AM: 287874, password: 287874Sotiris!)
INSERT OR IGNORE INTO users (am, first_name, last_name, date_of_birth, role, password) 
VALUES (287874, 'Admin', 'User', '1990-01-01', 'admin', '$2a$10$34XHuiX2ZNtSNUaln7HLb.t5hDaGNizbRIBgJ9XsFw2OOVPTTBdiK');

-- Insert test user (password: 1234)
INSERT OR IGNORE INTO users (am, first_name, last_name, date_of_birth, role, password) 
VALUES (12345, 'Test', 'User', '1995-05-15', 'user', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
