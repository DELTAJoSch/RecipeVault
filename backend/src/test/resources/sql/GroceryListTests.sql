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

INSERT INTO recipes (id, name, description, owner, difficulty)
VALUES (-1, 'test recipe one', 'test description one', -42, 'EASY');
INSERT INTO recipes (id, name, description, owner, difficulty)
VALUES (-2, 'test recipe two', 'test description one', -42, 'EASY');
INSERT INTO recipes (id, name, description, owner, difficulty)
VALUES (-3, 'test recipe three', 'test description one', -42, 'EASY');
INSERT INTO recipes (id, name, description, owner, difficulty)
VALUES (-4, 'test recipe four', 'test description two', -43, 'EASY');
INSERT INTO recipes (id, name, description, owner, difficulty)
VALUES (-5, 'admin deleteable recipe', 'eh der guade', -43, 'MEDIUM');


INSERT INTO lists (name, user_id)
VALUES ('my wonderful list 2', -43);
INSERT INTO lists_recipes (recipe_lists_name, recipe_lists_user_id, recipes_id)
VALUES ('my wonderful list 2', -43, -3);
INSERT INTO lists_recipes (recipe_lists_name, recipe_lists_user_id, recipes_id)
VALUES ('my wonderful list 2', -43, -4);

INSERT INTO lists (name, user_id)
VALUES ('list', -43);

INSERT INTO lists (name, user_id)
VALUES ('list', -42);
INSERT INTO lists_recipes (recipe_lists_name, recipe_lists_user_id, recipes_id)
VALUES ('list', -42, -4);
INSERT INTO lists_recipes (recipe_lists_name, recipe_lists_user_id, recipes_id)
VALUES ('list', -42, -1);

INSERT INTO ingredients (id, name, category)
VALUES (-1, 'test ingredient one', 'CHEESE_CREAM');
INSERT INTO ingredients (id, name, category)
VALUES (-2, 'TestIngredient', 'SHELLFISH');
INSERT INTO ingredients (id, name, category)
VALUES (-3, 'TestIngredi', 'CHEESE_DELICATE');
INSERT INTO ingredients (id, name, category)
VALUES (-4, 'TestIng', 'FISH');

INSERT INTO amount (recipe, ingredient, amount, unit)
VALUES (-1, -1, 100, 'KG');
INSERT INTO amount (recipe, ingredient, amount, unit)
VALUES (-1, -2, 100, 'G');
INSERT INTO amount (recipe, ingredient, amount, unit)
VALUES (-1, -3, 100, 'PIECE');
INSERT INTO amount (recipe, ingredient, amount, unit)
VALUES (-4, -2, 120, 'ML');
INSERT INTO amount (recipe, ingredient, amount, unit)
VALUES (-4, -4, 50, 'CUP');
INSERT INTO amount (recipe, ingredient, amount, unit)
VALUES (-4, -3, 10, 'KG');