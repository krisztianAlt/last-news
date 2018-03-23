INSERT INTO users (id, email, user_name, password, enabled, activation) VALUES (1, 'meropa@freemail.hu', 'Teszt Ember', 'pass', TRUE, '');

INSERT INTO role (id, role) VALUES (1, 'USER');

INSERT INTO user_role (user_id, role_id) VALUES (1, 1);

SELECT pg_catalog.setval('users_id_seq', 1, true);
SELECT pg_catalog.setval('role_id_seq', 1, true);
