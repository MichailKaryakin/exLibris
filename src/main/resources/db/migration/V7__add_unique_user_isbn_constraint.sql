CREATE UNIQUE INDEX uk_books_user_isbn
    ON books (user_id, isbn)
    WHERE isbn IS NOT NULL;