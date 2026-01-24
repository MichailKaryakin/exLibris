CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     role VARCHAR(20) NOT NULL,
                                     created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS books (
                                     id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     title VARCHAR(255),
                                     author VARCHAR(255),
                                     year INTEGER,
                                     description VARCHAR(2000),
                                     user_id BIGINT,
                                     CONSTRAINT fk_book_user FOREIGN KEY (user_id)
                                         REFERENCES users (id)
                                         ON DELETE SET NULL
                                         ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS reading (
                                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                       user_id BIGINT NOT NULL REFERENCES users(id),
                                       book_id BIGINT NOT NULL REFERENCES books(id),
                                       status VARCHAR(20) NOT NULL,
                                       score INT,
                                       finished_at TIMESTAMP,
                                       notes TEXT,
                                       current_page INT
);
