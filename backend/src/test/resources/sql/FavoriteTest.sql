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
VALUES (-402, 'testi@supi.xyz', 'unused', TRUE);
INSERT INTO users (id, email, password, admin)
VALUES (-403, 'testi@wow.xyz', 'unused', FALSE);
INSERT INTO users (id, email, password, admin)
VALUES (-404, 'testu', 'unused', FALSE);

INSERT INTO authors(id, firstname, lastname, description)
VALUES (-1, 'one', 'author', 'test');
INSERT INTO authors(id, firstname, lastname, description)
VALUES (-2, 'two', 'author', 'test');
INSERT INTO authors(id, firstname, lastname)
VALUES (-3, 'three', 'author');

INSERT INTO recipes (id, name, description, owner, difficulty, author)
VALUES (-1, 'Potatoes', 'Tasty potatoes with salt', -402, 'MEDIUM', -1);
INSERT INTO recipes (id, name, description, owner, difficulty, author)
VALUES (-3, 'Pizza', 'Wow, so good', -403, 'HARD', -1);
INSERT INTO recipes (id, name, description, owner, difficulty, author)
VALUES (-4, 'Cornflakes', 'Easy and fast', -404, 'EASY', -1);
INSERT INTO recipes (id, name, description, owner, difficulty, author)
VALUES (-5, 'Pancakes', 'Perfect for a romantic dinner', -402, 'EASY', -1);

INSERT INTO favorites (user_id, recipe_id)
VALUES (-402, -1);
INSERT INTO favorites (user_id, recipe_id)
VALUES (-402, -5);
INSERT INTO favorites (user_id, recipe_id)
VALUES (-402, -4);
INSERT INTO favorites (user_id, recipe_id)
VALUES (-403, -3);
INSERT INTO favorites (user_id, recipe_id)
VALUES (-403, -1);