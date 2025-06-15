-- Crea le sequenze in modo coerente
CREATE SEQUENCE IF NOT EXISTS author_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS book_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS credentials_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS review_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS user_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS image_seq START WITH 1;


--INSERT INTO author (id, name, surname, date_of_birth, date_of_death, nationality) VALUES (400, 'Geronimo', 'Stilton', '2020-06-15', '2024-06-15', 'italiana')
--INSERT INTO book (id, title, year, description) VALUES (400, 'Il libro della giungla', 2003, 'Il libro della giungla descrizione')
--INSERT INTO book_authors (authors_id, books_id) VALUES (400, 400)

--INSERT INTO author (id, name, surname, date_of_birth, date_of_death, nationality) VALUES (401, 'Alessandro', 'Manzoni', '1785-03-07', '1873-05-22', 'italiana');
--INSERT INTO book (id, title, year, description) VALUES (401, 'I Promessi Sposi', 1827, 'Romanzo storico ambientato nella Lombardia del XVII secolo');
--INSERT INTO book_authors (authors_id, books_id) VALUES (401, 401);

--INSERT INTO author (id, name, surname, date_of_birth, date_of_death, nationality) VALUES (402, 'Italo', 'Calvino', '1923-10-15', '1985-09-19', 'italiana');
--INSERT INTO book (id, title, year, description) VALUES (402, 'Il barone rampante', 1957, 'Storia di un ragazzo che decide di vivere sugli alberi');
--INSERT INTO book_authors (authors_id, books_id) VALUES (402, 402);

--INSERT INTO author (id, name, surname, date_of_birth, date_of_death, nationality) VALUES (403, 'Umberto', 'Eco', '1932-01-05', '2016-02-19', 'italiana');
--INSERT INTO book (id, title, year, description) VALUES (403, 'Il nome della rosa', 1980, 'Un giallo storico ambientato in un monastero medievale');
--INSERT INTO book_authors (authors_id, books_id) VALUES (403, 403);

