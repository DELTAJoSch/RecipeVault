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

INSERT INTO wines (id, name, vinyard, category, temperature, owner)
VALUES (-1, 'test wine one', 'test vinyard one', 'SPARKLING', 10, -42);
INSERT INTO wines (id, name, vinyard, category, temperature, owner)
VALUES (-2, 'test wine two', 'test vinyard one', 'SPARKLING', 10, -42);
INSERT INTO wines (id, name, vinyard, category, temperature, owner)
VALUES (-3, 'test wine three', 'test vinyard one', 'ROSE', 10, -42);
INSERT INTO wines (id, name, vinyard, category, temperature, owner)
VALUES (-4, 'test wine four', 'test vinyard two', 'SPARKLING', 10, -42);
INSERT INTO wines (id, name, vinyard, category, temperature, owner)
VALUES (-5, 'admin deleteable wine', 'eh der guade', 'FULL_WHITE', 10, -44);