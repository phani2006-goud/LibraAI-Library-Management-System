CREATE DATABASE IF NOT EXISTS library_db;

USE library_db;

CREATE TABLE IF NOT EXISTS books (
    book_id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100),
    category VARCHAR(100),
    rack_no VARCHAR(20),
    shelf_no VARCHAR(20),
    quantity INT DEFAULT 0,
    PRIMARY KEY (book_id)
);

TRUNCATE TABLE books;
