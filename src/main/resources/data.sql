CREATE TABLE users
(
    id       UUID PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255)
);

CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE user_roles
(
    user_id UUID,
    role_id SERIAL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE tweets
(
    id                 SERIAL PRIMARY KEY,
    user_id            UUID,
    content            TEXT,
    creation_timestamp TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);


INSERT INTO roles(id, name)
VALUES (1, 'ADMIN');
INSERT INTO roles(id, name)
VALUES (2, 'BASIC');