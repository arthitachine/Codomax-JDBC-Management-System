CREATE DATABASE IF NOT EXISTS codomax_db;
USE codomax_db;

CREATE TABLE IF NOT EXISTS employees (
    emp_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    department VARCHAR(60) NOT NULL,
    salary DOUBLE NOT NULL CHECK (salary >= 0)
);
