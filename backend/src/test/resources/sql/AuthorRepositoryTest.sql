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

INSERT INTO users (id, email, password, admin)
VALUES (-42, 'admin@test.xo', 'unused', TRUE);
INSERT INTO users (id, email, password, admin)
VALUES (-43, 'user@test.xo', 'unused', FALSE);
INSERT INTO users (id, email, password, admin)
VALUES (-44, 'user2@test.xo', 'unused', FALSE);

INSERT INTO authors(id, firstname, lastname, description)
VALUES (-1, 'one', 'author', 'test');
INSERT INTO authors(id, firstname, lastname, description)
VALUES (-2, 'two', 'author', 'test');
INSERT INTO authors(id, firstname, lastname)
VALUES (-3, 'three', 'author');