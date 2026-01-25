ALTER TABLE reading
    ALTER COLUMN user_id SET NOT NULL,
    ALTER COLUMN book_id SET NOT NULL;

ALTER TABLE reading
    ALTER COLUMN status SET NOT NULL;

UPDATE reading
SET finished_at = NULL
WHERE status <> 'FINISHED';

ALTER TABLE reading
    ADD COLUMN IF NOT EXISTS current_page INTEGER;

UPDATE reading
SET current_page = 0
WHERE current_page IS NULL OR current_page < 0;

ALTER TABLE reading
    DROP CONSTRAINT IF EXISTS reading_book_id_fkey;

ALTER TABLE reading
    ADD CONSTRAINT reading_book_id_fkey
        FOREIGN KEY (book_id)
            REFERENCES books(id)
            ON DELETE CASCADE;

ALTER TABLE reading
    DROP CONSTRAINT IF EXISTS reading_user_id_fkey;

ALTER TABLE reading
    ADD CONSTRAINT reading_user_id_fkey
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE;

CREATE INDEX IF NOT EXISTS idx_reading_user_id
    ON reading(user_id);

CREATE INDEX IF NOT EXISTS idx_reading_user_status
    ON reading(user_id, status);