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
VALUES (-52, 'test@12121262.com', 'unused', TRUE);
INSERT INTO users (id, email, password, admin)
VALUES (-53, 'test2@12121262.com', 'unused', FALSE);
INSERT INTO users (id, email, password, admin)
VALUES (-54, 'test3@12121262.com', 'unused', FALSE);

INSERT INTO ingredients (id, name, category)
VALUES (-1, 'test ingredient one', 'CHEESE_CREAM');
INSERT INTO ingredients (id, name, category)
VALUES (-2, 'TestIngredient', 'SHELLFISH');
INSERT INTO ingredients (id, name, category)
VALUES (-3, 'TestIngredi', 'CHEESE_DELICATE');
INSERT INTO ingredients (id, name, category)
VALUES (-4, 'TestIng', 'FISH');
