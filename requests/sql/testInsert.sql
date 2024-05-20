insert into items (uuid, title, price) values ('85884d50-9e78-45c0-ad50-4d83ec46a5d4', 'Apple', 10);
insert into items (uuid, title, price) values ('9233cbd9-6450-4fd7-a453-014ea7e9a5f6', 'PineApple', 0);
insert into items (uuid, title, price) VALUES ('a6dada73-e108-49a8-b238-3ae17b27e48d', 'Peach', 100) returning uuid, title, price;