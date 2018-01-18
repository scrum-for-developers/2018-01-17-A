-- liquibase formatted sql

-- changeset action:adjust index
DROP INDEX isbn ON book;
CREATE UNIQUE INDEX isbn_edition_year ON book (isbn, edition, year_of_publication);
