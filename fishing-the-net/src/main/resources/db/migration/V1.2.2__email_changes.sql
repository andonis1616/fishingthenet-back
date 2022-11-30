ALTER TABLE email
    ADD IF NOT EXISTS is_fishing boolean;

ALTER TABLE email
    ADD IF NOT EXISTS date_received timestamp;

-- insert into users(name, username, email, password)
-- values ('Nume test', 'user', 'email@gmail.com', '$2a$10$tCB1amB7gNnrjiMtrEivgOtw0ZSirAcvfx.dANa8Qgc8bfqE/eXGa');
--
-- insert into user_roles(user_id, role_id)
-- values ( 1, 1);
