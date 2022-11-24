CREATE TABLE email
(
    id       BIGINT generated always as identity,
    sender   text,
    content  text   NOT NULL,
    subject  text   NOT NULL,
    owner_id BIGINT NOT NULL,
    FOREIGN KEY (owner_id) references users (id),
    PRIMARY KEY (id),
    UNIQUE (id)
);