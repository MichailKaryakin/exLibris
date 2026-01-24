ALTER TABLE books
    ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE books
    DROP CONSTRAINT IF EXISTS fk_book_user;

ALTER TABLE books
    ADD CONSTRAINT fk_book_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE books
    ADD COLUMN total_pages INTEGER;

ALTER TABLE books
    ALTER COLUMN title SET NOT NULL,
    ALTER COLUMN author SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_books_user_id
    ON books(user_id);

CREATE INDEX IF NOT EXISTS idx_books_user_title
    ON books(user_id, title);

CREATE INDEX IF NOT EXISTS idx_books_user_author
    ON books(user_id, author);

ALTER TABLE reading
    DROP CONSTRAINT IF EXISTS reading_book_id_fkey;

ALTER TABLE reading
    ADD CONSTRAINT reading_book_id_fkey
        FOREIGN KEY (book_id)
            REFERENCES books(id)
            ON DELETE CASCADE;