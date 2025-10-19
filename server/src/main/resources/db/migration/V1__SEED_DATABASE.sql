-- ======================================
--  Flyway Migration: V1__seed_default_users.sql
--  Description: Creates tables if not exist and inserts default SUPER_ADMIN, ADMIN and REGULAR users
-- ======================================

-- -------------------
-- Table: user
-- -------------------
CREATE TABLE IF NOT EXISTS `user` (
                                      `id` INT NOT NULL AUTO_INCREMENT,
                                      `username` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255),
    `phone` VARCHAR(50),
    `name` VARCHAR(255),
    `user_type` VARCHAR(50),
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------
-- Table: role
-- -------------------
CREATE TABLE IF NOT EXISTS `role` (
                                      `id` INT NOT NULL AUTO_INCREMENT,
                                      `name` VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------
-- Table: permission
-- -------------------
CREATE TABLE IF NOT EXISTS `permission` (
                                            `id` INT NOT NULL AUTO_INCREMENT,
                                            `name` VARCHAR(255) NOT NULL,
    `permission_type` VARCHAR(50),
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------
-- Table: access_profile
-- -------------------
CREATE TABLE IF NOT EXISTS `access_profile` (
                                                `id` INT NOT NULL AUTO_INCREMENT,
                                                `name` VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------
-- Join Tables
-- -------------------
CREATE TABLE IF NOT EXISTS `users_roles` (
                                             `user_id` INT NOT NULL,
                                             `role_id` INT NOT NULL,
                                             PRIMARY KEY (`user_id`, `role_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `roles_permissions` (
                                                   `role_id` INT NOT NULL,
                                                   `permission_id` INT NOT NULL,
                                                   PRIMARY KEY (`role_id`, `permission_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `user_access_profile` (
                                                     `user_id` INT NOT NULL,
                                                     `access_profile_id` INT NOT NULL,
                                                     PRIMARY KEY (`user_id`, `access_profile_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -------------------
-- Insert default SUPER_ADMIN user
-- -------------------
INSERT INTO user (id, username, password, email, name, user_type, is_deleted)
SELECT 10000,
       'sadmin',
       '$2a$10$ket7oJYgBFkPiREOQX/kJOAeB16TYSxt7fxxFbUPT1MUu8QrLg5.m',
       'superadmin@domain.com',
       'Super Admin',
       'SUPER_ADMIN',
       0
    WHERE NOT EXISTS (SELECT 1 FROM user WHERE username = 'sadmin');

-- -------------------
-- Insert default ADMIN user
-- -------------------
INSERT INTO user (username, password, email, name, user_type, is_deleted)
SELECT 'admin',
       '$2a$10$ket7oJYgBFkPiREOQX/kJOAeB16TYSxt7fxxFbUPT1MUu8QrLg5.m',
       'admin@domain.com',
       'Administrator',
       'ADMIN',
       0
    WHERE NOT EXISTS (SELECT 1 FROM user WHERE username = 'admin');

-- -------------------
-- Insert default REGULAR user
-- -------------------
INSERT INTO user (username, password, email, name, user_type, is_deleted)
SELECT 'user',
       '$2a$10$ket7oJYgBFkPiREOQX/kJOAeB16TYSxt7fxxFbUPT1MUu8QrLg5.m',
       'user@domain.com',
       'Regular User',
       'REGULAR',
       0
    WHERE NOT EXISTS (SELECT 1 FROM user WHERE username = 'user');
