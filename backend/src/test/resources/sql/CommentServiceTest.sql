DELETE
FROM note;
DELETE
FROM comments;
DELETE
FROM wines;
DELETE
FROM amount;
DELETE
FROM ingredients;
DELETE
FROM application_options;
DELETE
FROM ocr_tasks;
DELETE
FROM favorites;
DELETE
FROM lists_recipes;
DELETE
FROM lists;
DELETE
FROM recipes;
DELETE
FROM users;
DELETE
FROM authors;

INSERT INTO users (id, email, password, admin) VALUES (-42, 'admin@example.com', 'unused', TRUE);
INSERT INTO users (id, email, password, admin) VALUES (-43, 'user@example.com', 'unused', FALSE);
INSERT INTO users (id, email, password, admin) VALUES (-44, 'user2@example.com', 'unused', FALSE);
INSERT INTO users (id, email, password, admin) VALUES (-45, 'x@example.com', 'unused', FALSE);

INSERT INTO recipes (id, name, description, owner, difficulty) VALUES (-1, 'test recipe one', 'test description one', -42, 'EASY');
INSERT INTO recipes (id, name, description, owner, difficulty) VALUES (-2, 'test recipe two', 'test description one', -42, 'EASY');
INSERT INTO recipes (id, name, description, owner, difficulty) VALUES (-3, 'test recipe three', 'test description one', -42, 'EASY');
INSERT INTO recipes (id, name, description, owner, difficulty) VALUES (-4, 'test recipe four', 'test description two', -42, 'EASY');

INSERT INTO comments ( CREATOR , RECIPE , DATE_TIME , CONTENT ) VALUES ( -43 , -1, '2023-05-10 15:20:03', 'sample comment content');
INSERT INTO comments ( CREATOR , RECIPE , DATE_TIME , CONTENT ) VALUES ( -43 , -4, '2023-05-10 16:17:09', 'sample comment content');
INSERT INTO comments ( CREATOR , RECIPE , DATE_TIME , CONTENT ) VALUES ( -43 , -3, '2023-05-10 15:20:03', 'sample comment content');
INSERT INTO comments ( CREATOR , RECIPE , DATE_TIME , CONTENT ) VALUES ( -45 , -1, '2023-05-12 19:56:13', 'sample comment content');