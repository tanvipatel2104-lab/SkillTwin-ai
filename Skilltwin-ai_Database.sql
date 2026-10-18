CREATE DATABASE IF NOT EXISTS skilltwin_db;
USE skilltwin_db;
-- 1. Students table
CREATE TABLE IF NOT EXISTS students (
    student_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    semester INT NOT NULL,
    email VARCHAR(150) UNIQUE,
    available_hours_per_day DECIMAL(4,2) DEFAULT 2.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- 2. Skills table
CREATE TABLE IF NOT EXISTS skills (
    skill_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    skill_name VARCHAR(100) NOT NULL,
    current_level INT NOT NULL DEFAULT 0,
    target_level INT NOT NULL DEFAULT 100,
    CONSTRAINT chk_current_level
        CHECK (current_level BETWEEN 0 AND 100),
    CONSTRAINT chk_target_level
        CHECK (target_level BETWEEN 0 AND 100),
    CONSTRAINT fk_skills_student
        FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE CASCADE
);
-- 3. Learning goals table
CREATE TABLE IF NOT EXISTS learning_goals (
    goal_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    goal_title VARCHAR(200) NOT NULL,
    description TEXT,
    target_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    CONSTRAINT fk_goals_student
        FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE CASCADE
);

-- 4. Learning scenarios table
CREATE TABLE IF NOT EXISTS learning_scenarios (
    scenario_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    scenario_name VARCHAR(150) NOT NULL,
    duration_weeks INT NOT NULL DEFAULT 4,
    estimated_score DECIMAL(5,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_scenarios_student
        FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE CASCADE
);

-- 5. Study plans table
CREATE TABLE IF NOT EXISTS study_plans (
    plan_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    plan_date DATE NOT NULL,
    skill_name VARCHAR(100) NOT NULL,
    planned_hours DECIMAL(4,2) NOT NULL DEFAULT 1.00,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_plans_student
        FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE CASCADE
);
-- 6. Progress records table
CREATE TABLE IF NOT EXISTS progress_records (
    progress_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    skill_id BIGINT,
    hours_completed DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    skill_level_after INT,
    notes TEXT,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_progress_student
        FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_progress_skill
        FOREIGN KEY (skill_id)
        REFERENCES skills(skill_id)
        ON DELETE SET NULL
);
