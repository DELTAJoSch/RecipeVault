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
VALUES (-44, 'user2@@example.com', 'unused', FALSE);
