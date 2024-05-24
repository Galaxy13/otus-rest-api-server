create database store;
grant all privileges on database store to postgres;
\c chat;

create table if not exists public.items
(
    uuid  uuid    not null primary key unique,
    title varchar not null,
    price int     not null
)