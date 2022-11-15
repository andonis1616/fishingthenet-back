CREATE TABLE roles
(
    id           BIGINT generated always as identity,
    name         text    NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (id)
);


INSERT INTO roles(name) VALUES('ROLE_ADMIN');
INSERT INTO roles(name) VALUES('ROLE_USER');

CREATE TABLE users
(
    id           BIGINT generated always as identity,
    name         text    NOT NULL,
    username         text    NOT NULL,
    email         text    NOT NULL,
    password         text    NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (id)
);

CREATE TABLE user_roles
(
    id          integer generated always as identity,
    user_id     BIGINT,
    role_id     BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);