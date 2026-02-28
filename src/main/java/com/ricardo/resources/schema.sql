-- Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    refresh_token VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Applications Table
CREATE TABLE IF NOT EXISTS applications (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    company VARCHAR(100) NOT NULL,
    role VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'APPLIED',
    salary_min DECIMAL(10,2),
    salary_max DECIMAL(10,2),
    location VARCHAR(100),
    is_remote BOOLEAN DEFAULT FALSE,
    logo_url VARCHAR(255),
    job_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    application_note TEXT,
    applied_date DATE NOT NULL,

    -- User Foreign key
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,

    -- Check Status
    CONSTRAINT chk_status CHECK (status IN ('APPLIED', 'INTERVIEW', 'OFFER', 'REJECTED')),

    -- Check Salary
    CONSTRAINT chk_salary_min CHECK (salary_min IS NULL OR salary_min > 0), -- If it isn't null, check that it's more than 0
    CONSTRAINT chk_salary_max CHECK (salary_max IS NULL OR salary_max > 0), -- If it isn't null, check that it's more than 0
    CONSTRAINT chk_salary CHECK (salary_min IS NULL OR salary_max IS NULL OR salary_max > salary_min) -- If neither of them are null, check that salary_max is more than salary_min
);

-- Status History Table
CREATE TABLE IF NOT EXISTS status_history (
    status_id INT AUTO_INCREMENT PRIMARY KEY,
    application_id INT NOT NULL,
    old_status VARCHAR(20),
    new_status VARCHAR(20) NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status_note TEXT,
    
    -- Foreign key
    CONSTRAINT fk_status_application FOREIGN KEY (application_id) REFERENCES applications(application_id) ON DELETE CASCADE,

    -- Check status
    CONSTRAINT chk_new_status CHECK (new_status IN ('APPLIED', 'INTERVIEW', 'OFFER', 'REJECTED')),
    CONSTRAINT chk_old_status CHECK (old_status IN ('APPLIED', 'INTERVIEW', 'OFFER', 'REJECTED'))
);

-- Interviews Table
CREATE TABLE IF NOT EXISTS interviews (
    interview_id INT AUTO_INCREMENT PRIMARY KEY,
    application_id INT NOT NULL,
    round_number INT NOT NULL,
    type VARCHAR(30) NOT NULL,
    scheduled_at TIMESTAMP,
    interviewer VARCHAR(100),
    outcome VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    interview_note TEXT,

    -- FK
    CONSTRAINT fk_interview_application FOREIGN KEY (application_id) REFERENCES applications(application_id) ON DELETE CASCADE,

    -- Check Type
    CONSTRAINT chk_type CHECK (type IN ('PHONE_SCREEN', 'TECHNICAL', 'SYSTEM_DESIGN', 'HR', 'FINAL')),

    -- Check Outcome
    CONSTRAINT chk_outcome CHECK (outcome IN ('PASSED', 'FAILED', 'PENDING'))
);

