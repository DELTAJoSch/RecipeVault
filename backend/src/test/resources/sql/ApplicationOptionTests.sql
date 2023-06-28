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

INSERT INTO application_options (id, name, default_value, type)
VALUES (-1, 'test option one', 'TRUE', 'BOOLEAN');
INSERT INTO application_options (id, name, default_value, type)
VALUES (-2, 'test option two', '65534', 'LONG_RANGE');
INSERT INTO application_options (id, name, default_value, type)
VALUES (-3, 'test option three', '128', 'SHORT_RANGE');
INSERT INTO application_options (id, name, default_value, type)
VALUES (-4, 'test option four', 'OTTOKAR', 'STRING');