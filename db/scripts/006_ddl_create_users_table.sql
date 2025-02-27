CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    email    varchar unique not null,
    name     varchar        not null,
    password varchar        not null
);