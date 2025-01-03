//For Creating DATABASE
CREATE DATABASE BookstoreDB ;


// For Creating Table
CREATE TABLE Books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    price int NOT NULL
);
