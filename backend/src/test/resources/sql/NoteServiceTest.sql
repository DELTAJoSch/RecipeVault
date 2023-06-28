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
VALUES (-42, 'admin@example.com', 'unused', TRUE);
INSERT INTO users (id, email, password, admin)
VALUES (-43, 'user@example.com', 'unused', FALSE);
INSERT INTO users (id, email, password, admin)
VALUES (-44, 'user2@example.com', 'unused', FALSE);
INSERT INTO users (id, email, password, admin)
VALUES (-45, 'x@example.com', 'unused', FALSE);

INSERT INTO authors(id, firstname, lastname, description)
VALUES (-1, 'one', 'author', 'test');
INSERT INTO authors(id, firstname, lastname, description)
VALUES (-2, 'two', 'author', 'test');
INSERT INTO authors(id, firstname, lastname)
VALUES (-3, 'three', 'author');

INSERT INTO recipes (id, name, description, owner, difficulty, author)
VALUES (-1, 'test recipe one', 'test description one', -42, 'EASY', -1);
INSERT INTO recipes (id, name, description, owner, difficulty, author)
VALUES (-2, 'test recipe two', 'test description one', -42, 'EASY', -1);
INSERT INTO recipes (id, name, description, owner, difficulty, author)
VALUES (-3, 'test recipe three', 'test description one', -42, 'EASY', -1);
INSERT INTO recipes (id, name, description, owner, difficulty, author)
VALUES (-4, 'test recipe four', 'test description two', -42, 'EASY', -1);
INSERT INTO recipes (id, name, description, owner, difficulty, author)
VALUES (-5, 'admin deleteable recipe', 'eh der guade', -44, 'MEDIUM', -1);

INsERT INTO NOTE (OWNER, RECIPE, CONTENT)
VALUES (-43, -1, 'sample note content');
INsERT INTO NOTE (OWNER, RECIPE, CONTENT)
VALUES (-43, -2, 'sample note content');
INsERT INTO NOTE (OWNER, RECIPE, CONTENT)
VALUES (-43, -3, 'sample note content');
INsERT INTO NOTE (OWNER, RECIPE, CONTENT)
VALUES (-45, -1, 'sample note content');