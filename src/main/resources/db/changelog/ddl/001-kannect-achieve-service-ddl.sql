-- BADGES TABLE
CREATE TABLE IF NOT EXISTS badges (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    badge_type VARCHAR(100) NOT NULL,
    recognition_points INTEGER NOT NULL DEFAULT 0,
    badge_image_url TEXT,
    CONSTRAINT unique_badge_type UNIQUE (badge_type)
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_badges_badge_type ON badges(badge_type);

-- TASKS TABLE
CREATE TABLE IF NOT EXISTS tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    assigned_to INTEGER NOT NULL,
    assigned_by INTEGER NOT NULL,
    deadline TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tasks_assigned_to FOREIGN KEY (assigned_to) REFERENCES users(id),
    CONSTRAINT fk_tasks_assigned_by FOREIGN KEY (assigned_by) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS idx_tasks_assigned_to ON tasks(assigned_to);
CREATE INDEX IF NOT EXISTS idx_tasks_deadline ON tasks(deadline);

-- RECOGNITIONS TABLE
CREATE TABLE IF NOT EXISTS recognitions (
    id SERIAL PRIMARY KEY,
    sender_user_id INTEGER NOT NULL,
    receiver_user_id INTEGER NOT NULL,
    badge_id INTEGER NOT NULL,
    message TEXT,
    image_url VARCHAR(500),
    approved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recognitions_badge FOREIGN KEY (badge_id) REFERENCES badges(id),
    CONSTRAINT fk_recognitions_sender FOREIGN KEY (sender_user_id) REFERENCES users(id),
    CONSTRAINT fk_recognitions_receiver FOREIGN KEY (receiver_user_id) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS idx_recognitions_sender ON recognitions(sender_user_id);
CREATE INDEX IF NOT EXISTS idx_recognitions_receiver ON recognitions(receiver_user_id);
CREATE INDEX IF NOT EXISTS idx_recognitions_badge_receiver ON recognitions(badge_id, receiver_user_id);

-- LEADERBOARD ENTRIES TABLE
CREATE TABLE IF NOT EXISTS leaderboard_entries (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    total_task_points INTEGER DEFAULT 0,
    total_recognition_points INTEGER DEFAULT 0,
    total_points INTEGER DEFAULT 0,
    period_start_date DATE NOT NULL,
    period_end_date DATE NOT NULL,
    period_type VARCHAR(50) NOT NULL, -- WEEKLY / MONTHLY / QUARTERLY
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_leaderboard_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS idx_leaderboard_user ON leaderboard_entries(user_id);
CREATE INDEX IF NOT EXISTS idx_leaderboard_period ON leaderboard_entries(period_start_date, period_end_date);
