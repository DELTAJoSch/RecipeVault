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
VALUES (-42, 'test@12122392.xyz', 'unused', TRUE);
INSERT INTO users (id, email, password, admin)
VALUES (-43, 'test.two@12122392.xyz', 'unused', FALSE);
INSERT INTO users (id, email, password, admin)
VALUES (-44, 'test.three@12122392.xyz', 'unused', FALSE);

INSERT INTO ingredients (id, name, category)
VALUES (-1, 'test ingredient one', 'CHEESE_CREAM');

INSERT INTO authors (id, firstname, lastname, description)
VALUES (-1, 'Jamie', 'Oliver', 'testdespription');

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

INSERT INTO amount (recipe, ingredient, amount, unit)
VALUES (-1, -1, 100, 'KG');