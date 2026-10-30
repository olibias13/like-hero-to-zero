CREATE DATABASE IF NOT EXISTS herotozero CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'herotozero'@'localhost' IDENTIFIED BY 'PASSWORT_HIER_EINTRAGEN';
GRANT ALL PRIVILEGES ON herotozero.* TO 'herotozero'@'localhost';