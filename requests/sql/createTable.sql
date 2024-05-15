create database store;
grant all privileges on database store to postgres;
\c chat;

create table if not exists public.items(
    item_id int generated always as identity primary key,
    uuid uuid not null,
    title varchar not null,
    price int not null
)