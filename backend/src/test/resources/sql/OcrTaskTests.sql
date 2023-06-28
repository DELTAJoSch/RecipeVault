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
FROM comments;
DELETE
FROM recipes;
DELETE
FROM users;
DELETE
FROM authors;

INSERT INTO ocr_tasks (id, status, step, name, creation_date)
VALUES (-1, FALSE, 'PENDING', 'pending.jpg', parsedatetime('01-01-2001 10:00:00.01', 'dd-MM-yyyy hh:mm:ss.SS'));
INSERT INTO ocr_tasks (id, status, step, name, creation_date)
VALUES (-2, FALSE, 'PREPARATION', 'preparation.jpg', parsedatetime('01-01-2001 10:00:00.01', 'dd-MM-yyyy hh:mm:ss.SS'));
INSERT INTO ocr_tasks (id, status, step, name, creation_date)
VALUES (-3, FALSE, 'READING', 'reading.jpg', parsedatetime('01-01-2001 10:00:00.01', 'dd-MM-yyyy hh:mm:ss.SS'));
INSERT INTO ocr_tasks (id, status, step, name, creation_date)
VALUES (-4, FALSE, 'FINISHED', 'finished.jpg', parsedatetime('01-01-2001 10:00:00.01', 'dd-MM-yyyy hh:mm:ss.SS'));