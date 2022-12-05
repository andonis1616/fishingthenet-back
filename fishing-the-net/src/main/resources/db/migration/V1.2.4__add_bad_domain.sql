CREATE TABLE bad_domain
(
    id       BIGINT generated always as identity,
    domain   text,
    PRIMARY KEY (id),
    UNIQUE (id)
);