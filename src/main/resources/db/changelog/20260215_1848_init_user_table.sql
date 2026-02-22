CREATE SCHEMA final_user;

CREATE TABLE final_user.user (
    id SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP NULL
);

CREATE UNIQUE INDEX user_idx01 ON final_user.user(username)
WHERE deleted_at IS NULL;
