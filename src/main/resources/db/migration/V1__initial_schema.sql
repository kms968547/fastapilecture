-- V1__initial_schema.sql
-- This script contains the initial schema for the JPA-managed entities.

CREATE TABLE `user` (
  `active` bit(1) NOT NULL,
  `birthdate` date NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `phonenumber` varchar(20) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `address` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `role` enum('ROLE_ADMIN','ROLE_USER') NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UKhl4ga9r00rh51mdaf20hmnslt` (`email`),
  UNIQUE KEY `UKaevijvmscua5vvy83kmpyiuow` (`phonenumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `book` (
  `available_copies` int DEFAULT NULL,
  `price` double NOT NULL,
  `publication_date` date DEFAULT NULL,
  `publication_year` int DEFAULT NULL,
  `total_copies` int DEFAULT NULL,
  `book_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `seller_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `isbn` varchar(17) NOT NULL,
  `genre` varchar(100) DEFAULT NULL,
  `summary` varchar(1000) DEFAULT NULL,
  `author` varchar(255) NOT NULL,
  `publisher` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`book_id`),
  UNIQUE KEY `UKehpdfjpu1jm3hijhj4mm0hx9h` (`isbn`),
  KEY `FKnh12aqw09fpudejpjw8wm5nh5` (`seller_id`),
  CONSTRAINT `FKnh12aqw09fpudejpjw8wm5nh5` FOREIGN KEY (`seller_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `refresh_token` (
  `created_at` datetime(6) NOT NULL,
  `expires_at` datetime(6) NOT NULL,
  `revoked_at` datetime(6) DEFAULT NULL,
  `token_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `ip` varchar(64) DEFAULT NULL,
  `token_hash` varchar(255) NOT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`token_id`),
  UNIQUE KEY `idx_refresh_token_token_hash` (`token_hash`),
  KEY `idx_refresh_token_user_id` (`user_id`),
  CONSTRAINT `FKdot1ba64yqsct4fohu7ci6e4f` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `review` (
  `rating` int NOT NULL,
  `book_id` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `review_id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `comment` varchar(1000) NOT NULL,
  PRIMARY KEY (`review_id`),
  KEY `FK70yrt09r4r54tcgkrwbeqenbs` (`book_id`),
  KEY `FKj8m0asijw8lfl4jxhcps8tf4y` (`user_id`),
  CONSTRAINT `FK70yrt09r4r54tcgkrwbeqenbs` FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`),
  CONSTRAINT `FKj8m0asijw8lfl4jxhcps8tf4y` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `order` (
  `order_date` datetime(6) NOT NULL,
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `status` enum('CANCELLED','DELIVERED','PENDING','PROCESSING','RETURNED','SHIPPED') NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `FKrcaf946w0bh6qj0ljiw3pwpnu` (`user_id`),
  CONSTRAINT `FKrcaf946w0bh6qj0ljiw3pwpnu` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `order_item` (
  `price` double NOT NULL,
  `quantity` int NOT NULL,
  `book_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `order_item_id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`order_item_id`),
  KEY `FKb033an1f8qmpbnfl0a6jb5njs` (`book_id`),
  KEY `FKs234mi6jususbx4b37k44cipy` (`order_id`),
  CONSTRAINT `FKb033an1f8qmpbnfl0a6jb5njs` FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`),
  CONSTRAINT `FKs234mi6jususbx4b37k44cipy` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
