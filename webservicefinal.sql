-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: webservicefinal
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `author`
--

DROP TABLE IF EXISTS `author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `author` (
  `author_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`author_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author`
--

LOCK TABLES `author` WRITE;
/*!40000 ALTER TABLE `author` DISABLE KEYS */;
/*!40000 ALTER TABLE `author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (5,25,'2020-01-01',2020,10,1,'2025-12-15 04:39:30.000000',1,'2025-12-15 04:39:30.000000','978-0000000001','Adventure','A thrilling adventure story.','A. Author','Publisher One','The Great Adventure'),(8,18.5,'2019-05-15',2019,8,2,'2025-12-15 04:39:30.000000',1,'2025-12-15 04:39:30.000000','978-0000000002','Mystery','Unravel the secrets of the lost key.','B. Writer','Publisher Two','Mystery of the Lost Key'),(12,30,'2021-03-10',2021,12,3,'2025-12-15 04:39:30.000000',1,'2025-12-15 04:39:30.000000','978-0000000003','Cooking','Simple recipes for new cooks.','C. Chef','Publisher Three','Cooking for Beginners'),(3,22.75,'2022-07-20',2022,5,4,'2025-12-15 04:39:30.000000',1,'2025-12-15 04:39:30.000000','978-0000000004','Science','Amazing facts about the universe.','D. Scientist','Publisher One','Science Facts Unveiled'),(7,40,'2018-11-01',2018,7,5,'2025-12-15 04:39:30.000000',1,'2025-12-15 04:39:30.000000','978-0000000005','History','Journey through ancient times.','E. Historian','Publisher Two','History of Ancient Civilizations');
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_author`
--

DROP TABLE IF EXISTS `book_author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_author` (
  `book_id` int NOT NULL,
  `author_id` int NOT NULL,
  `display_order` int NOT NULL,
  PRIMARY KEY (`book_id`,`author_id`),
  KEY `fk_book_author_author` (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_author`
--

LOCK TABLES `book_author` WRITE;
/*!40000 ALTER TABLE `book_author` DISABLE KEYS */;
/*!40000 ALTER TABLE `book_author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_category`
--

DROP TABLE IF EXISTS `book_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_category` (
  `book_id` int NOT NULL,
  `category_id` int NOT NULL,
  PRIMARY KEY (`book_id`,`category_id`),
  KEY `fk_book_category_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_category`
--

LOCK TABLES `book_category` WRITE;
/*!40000 ALTER TABLE `book_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `book_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `parent_id` int DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`category_id`),
  KEY `fk_category_parent` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `review_id` int NOT NULL,
  `content` text NOT NULL,
  `comment_like_count` int NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_id`),
  KEY `fk_comment_user` (`user_id`),
  KEY `fk_comment_review` (`review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupon`
--

DROP TABLE IF EXISTS `coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coupon` (
  `coupon_id` int NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `discount_rate` int NOT NULL,
  `start_at` timestamp NOT NULL,
  `end_at` timestamp NOT NULL,
  `is_valid` tinyint(1) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`coupon_id`),
  KEY `fk_coupon_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupon`
--

LOCK TABLES `coupon` WRITE;
/*!40000 ALTER TABLE `coupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `coupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorite`
--

DROP TABLE IF EXISTS `favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite` (
  `favorite_id` int NOT NULL AUTO_INCREMENT,
  `book_id` int NOT NULL,
  `user_id` bigint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`favorite_id`),
  KEY `fk_favorite_book` (`book_id`),
  KEY `fk_favorite_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorite`
--

LOCK TABLES `favorite` WRITE;
/*!40000 ALTER TABLE `favorite` DISABLE KEYS */;
/*!40000 ALTER TABLE `favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
INSERT INTO `flyway_schema_history` VALUES (1,'1','<< Flyway Baseline >>','BASELINE','<< Flyway Baseline >>',NULL,'root','2025-12-14 19:39:29',0,1),(2,'2','seed data','SQL','V2__seed_data.sql',1435527889,'root','2025-12-14 19:39:30',7,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `order_date` datetime(6) NOT NULL,
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `status` enum('CANCELLED','DELIVERED','PENDING','PROCESSING','RETURNED','SHIPPED') NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `FKrcaf946w0bh6qj0ljiw3pwpnu` (`user_id`),
  CONSTRAINT `FKrcaf946w0bh6qj0ljiw3pwpnu` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES ('2025-12-15 04:39:30.000000',1,2,'DELIVERED'),('2025-12-15 04:39:30.000000',2,3,'PENDING'),('2025-12-15 04:39:30.000000',3,4,'SHIPPED'),('2025-12-15 04:39:30.000000',4,5,'DELIVERED'),('2025-12-15 04:39:30.000000',5,6,'CANCELLED');
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (25,1,1,1,1),(18.5,1,2,1,2),(30,2,3,2,3),(22.75,1,4,3,4),(40,1,5,4,5),(25,1,1,5,6);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderitem`
--

DROP TABLE IF EXISTS `orderitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderitem` (
  `order_item_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `book_id` int NOT NULL,
  `quantity` int NOT NULL,
  `amount` decimal(14,2) NOT NULL,
  `status` enum('PREPARED','CREATED','SHIPPED') NOT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`order_item_id`),
  KEY `fk_order_item_order` (`order_id`),
  KEY `fk_order_item_book` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderitem`
--

LOCK TABLES `orderitem` WRITE;
/*!40000 ALTER TABLE `orderitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `orderitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_token`
--

DROP TABLE IF EXISTS `refresh_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_token`
--

LOCK TABLES `refresh_token` WRITE;
/*!40000 ALTER TABLE `refresh_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `refresh_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (5,1,'2025-12-15 04:39:30.000000',1,'2025-12-15 04:39:30.000000',2,'Absolutely loved it! A must-read.'),(4,1,'2025-12-15 04:39:30.000000',2,'2025-12-15 04:39:30.000000',3,'Good, but a bit slow in the middle.'),(5,2,'2025-12-15 04:39:30.000000',3,'2025-12-15 04:39:30.000000',4,'Kept me guessing until the end.'),(4,3,'2025-12-15 04:39:30.000000',4,'2025-12-15 04:39:30.000000',5,'Very practical and easy to follow.'),(3,4,'2025-12-15 04:39:30.000000',5,'2025-12-15 04:39:30.000000',6,'Informative, but sometimes too dense.'),(5,5,'2025-12-15 04:39:30.000000',6,'2025-12-15 04:39:30.000000',7,'Fascinating insights into the past.'),(3,1,'2025-12-15 04:39:30.000000',7,'2025-12-15 04:39:30.000000',8,'Could be better, pacing issues.'),(4,2,'2025-12-15 04:39:30.000000',8,'2025-12-15 04:39:30.000000',9,'Decent read for a mystery fan.'),(5,3,'2025-12-15 04:39:30.000000',9,'2025-12-15 04:39:30.000000',10,'My go-to cookbook now!'),(4,4,'2025-12-15 04:39:30.000000',10,'2025-12-15 04:39:30.000000',2,'Learned a lot, well structured.');
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review_like`
--

DROP TABLE IF EXISTS `review_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review_like` (
  `review_id` int NOT NULL,
  `user_id` bigint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`review_id`,`user_id`),
  KEY `fk_review_like_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review_like`
--

LOCK TABLES `review_like` WRITE;
/*!40000 ALTER TABLE `review_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `review_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seller`
--

DROP TABLE IF EXISTS `seller`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seller` (
  `seller_id` int NOT NULL AUTO_INCREMENT,
  `business_name` varchar(100) NOT NULL,
  `business_number` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phonenumber` varchar(50) DEFAULT NULL,
  `address` varchar(255) NOT NULL,
  `payoutbank` varchar(50) NOT NULL,
  `payoutaccount` varchar(100) NOT NULL,
  `payoutholder` varchar(30) NOT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`seller_id`),
  UNIQUE KEY `business_name` (`business_name`),
  UNIQUE KEY `business_number` (`business_number`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `payoutaccount` (`payoutaccount`),
  UNIQUE KEY `phonenumber` (`phonenumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seller`
--

LOCK TABLES `seller` WRITE;
/*!40000 ALTER TABLE `seller` DISABLE KEYS */;
/*!40000 ALTER TABLE `seller` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settlement`
--

DROP TABLE IF EXISTS `settlement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settlement` (
  `settlement_id` int NOT NULL AUTO_INCREMENT,
  `seller_id` int NOT NULL,
  `period_start` timestamp NOT NULL,
  `period_end` timestamp NOT NULL,
  `total_sales` decimal(14,2) NOT NULL,
  `commission` decimal(5,2) NOT NULL,
  `final_payout` decimal(14,2) NOT NULL,
  `settlement_date` timestamp NOT NULL,
  PRIMARY KEY (`settlement_id`),
  KEY `fk_settlement_seller` (`seller_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settlement`
--

LOCK TABLES `settlement` WRITE;
/*!40000 ALTER TABLE `settlement` DISABLE KEYS */;
/*!40000 ALTER TABLE `settlement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (_binary '','1980-01-01','2025-12-15 04:39:30.000000','2025-12-15 04:39:30.000000',1,'01010000000','Admin User','$2a$10$w0D7Yd.S8.L2.J7Y7G1.X.z.T.G.Z.H.J.K.L.M.N.O.P.Q.R.S.T.U.V.W.X.Y.Z.','Admin Address','admin@example.com','MALE','ROLE_ADMIN'),(_binary '','1990-01-01','2025-12-15 04:39:30.000000','2025-12-15 04:39:30.000000',2,'01011111111','User One','$2a$10$w0D7Yd.S8.L2.J7Y7G1.X.z.T.G.Z.H.J.K.L.M.N.O.P.Q.R.S.T.U.V.W.X.Y.Z.','Address 1','user1@example.com','MALE','ROLE_USER'),(_binary '','1991-02-02','2025-12-15 04:39:30.000000','2025-12-15 04:39:30.000000',3,'01022222222','User Two','$2a$10$w0D7Yd.S8.L2.J7Y7G1.X.z.T.G.Z.H.J.K.L.M.N.O.P.Q.R.S.T.U.V.W.X.Y.Z.','Address 2','user2@example.com','FEMALE','ROLE_USER'),(_binary '','1992-03-03','2025-12-15 04:39:30.000000','2025-12-15 04:39:30.000000',4,'01033333333','User Three','$2a$10$w0D7Yd.S8.L2.J7Y7G1.X.z.T.G.Z.H.J.K.L.M.N.O.P.Q.R.S.T.U.V.W.X.Y.Z.','Address 3','user3@example.com','MALE','ROLE_USER'),(_binary '','1993-04-04','2025-12-15 04:39:30.000000','2025-12-15 04:39:30.000000',5,'01044444444','User Four','$2a$10$w0D7Yd.S8.L2.J7Y7G1.X.z.T.G.Z.H.J.K.L.M.N.O.P.Q.R.S.T.U.V.W.X.Y.Z.','Address 4','user4@example.com','FEMALE','ROLE_USER'),(_binary '','1994-05-05','2025-12-15 04:39:30.000000','2025-12-15 04:39:30.000000',6,'01055555555','User Five','$2a$10$w0D7Yd.S8.L2.J7Y7G1.X.z.T.G.Z.H.J.K.L.M.N.O.P.Q.R.S.T.U.V.W.X.Y.Z.','Address 5','user5@example.com','MALE','ROLE_USER'),(_binary '','1995-06-06','2025-12-15 04:39:30.000000','2025-12-15 04:39:30.000000',7,'01066666666','User Six','$2a$10$w0D7Yd.S8.L2.J7Y7G1.X.z.T.G.Z.H.J.K.L.M.N.O.P.Q.R.S.T.U.V.W.X.Y.Z.','Address 6','user6@example.com','FEMALE','ROLE_USER'),(_binary '','1996-07-07','2025-12-15 04:39:30.000000','2025-12-15 04:39:30.000000',8,'01077777777','User Seven','$2a$10$w0D7Yd.S8.L2.J7Y7G1.X.z.T.G.Z.H.J.K.L.M.N.O.P.Q.R.S.T.U.V.W.X.Y.Z.','Address 7','user7@example.com','MALE','ROLE_USER'),(_binary '','1997-08-08','2025-12-15 04:39:30.000000','2025-12-15 04:39:30.000000',9,'01088888888','User Eight','$2a$10$w0D7Yd.S8.L2.J7Y7G1.X.z.T.G.Z.H.J.K.L.M.N.O.P.Q.R.S.T.U.V.W.X.Y.Z.','Address 8','user8@example.com','FEMALE','ROLE_USER'),(_binary '','1998-09-09','2025-12-15 04:39:30.000000','2025-12-15 04:39:30.000000',10,'01099999999','User Nine','$2a$10$w0D7Yd.S8.L2.J7Y7G1.X.z.T.G.Z.H.J.K.L.M.N.O.P.Q.R.S.T.U.V.W.X.Y.Z.','Address 9','user9@example.com','MALE','ROLE_USER'),(_binary '','1999-10-10','2025-12-15 04:39:30.000000','2025-12-15 04:39:30.000000',11,'01010101010','User Ten','$2a$10$w0D7Yd.S8.L2.J7Y7G1.X.z.T.G.Z.H.J.K.L.M.N.O.P.Q.R.S.T.U.V.W.X.Y.Z.','Address 10','user10@example.com','FEMALE','ROLE_USER');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_coupon`
--

DROP TABLE IF EXISTS `user_coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_coupon` (
  `user_id` bigint NOT NULL,
  `coupon_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`coupon_id`),
  KEY `fk_user_coupon_coupon` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_coupon`
--

LOCK TABLES `user_coupon` WRITE;
/*!40000 ALTER TABLE `user_coupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_coupon` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-15 13:45:57
