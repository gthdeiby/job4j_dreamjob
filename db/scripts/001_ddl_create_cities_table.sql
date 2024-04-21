create table cities
(
    id   serial primary key,
    name varchar not null unique
);