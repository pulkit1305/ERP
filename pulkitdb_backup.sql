-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: manik_institute
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `Username` varchar(1000) DEFAULT NULL,
  `Password` varchar(1000) DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `email_2` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('Raj','Raj@123',1,'Raj@gmail.com');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admissions`
--

DROP TABLE IF EXISTS `admissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admissions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `course_id` int DEFAULT NULL,
  `date_of_admission` date DEFAULT NULL,
  `amount_paid` int DEFAULT '0',
  `reference_number` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `reference_number` (`reference_number`),
  KEY `course_id` (`course_id`),
  KEY `fk_user_id` (`user_id`),
  CONSTRAINT `admissions_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admissions`
--

LOCK TABLES `admissions` WRITE;
/*!40000 ALTER TABLE `admissions` DISABLE KEYS */;
INSERT INTO `admissions` VALUES (133,161,3,'2025-01-18',1000,'REF202501188933'),(134,161,4,'2025-01-18',1000,'REF202501185666'),(135,161,5,'2025-01-18',5000,'REF20250118929'),(136,161,2,'2025-01-18',1000,'REF202501182525');
/*!40000 ALTER TABLE `admissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `companies`
--

DROP TABLE IF EXISTS `companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companies` (
  `company_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `database_name` varchar(100) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companies`
--

LOCK TABLES `companies` WRITE;
/*!40000 ALTER TABLE `companies` DISABLE KEYS */;
/*!40000 ALTER TABLE `companies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_users`
--

DROP TABLE IF EXISTS `company_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company_users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `company_id` int DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `company_id` (`company_id`),
  CONSTRAINT `company_users_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_users`
--

LOCK TABLES `company_users` WRITE;
/*!40000 ALTER TABLE `company_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `company_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `id` int NOT NULL DEFAULT '1',
  `name` varchar(1000) DEFAULT NULL,
  `description` text,
  `fees` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,'JAVA','OOPs',8000),(2,'React','should know javascript',2000),(3,'JAVA+REACT','JAVA is used as a backend and react is used for a front end \n',20000),(4,'DATA ANALYSIS','In Data Analysis we have  Power BI ,Tabalue etc.',30000),(5,'Cloud Computing ','Cloud computing is a way to access IT resources, like storage, computing power, and databases, over the internet on a pay-as-you-go basis.',45000);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emp`
--

DROP TABLE IF EXISTS `emp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emp` (
  `id` int NOT NULL,
  `username` varchar(1000) DEFAULT NULL,
  `password` varchar(1000) DEFAULT NULL,
  `Date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emp`
--

LOCK TABLES `emp` WRITE;
/*!40000 ALTER TABLE `emp` DISABLE KEYS */;
INSERT INTO `emp` VALUES (1,'pulkit','sharma','2024-11-18 08:02:43'),(2,'Raju','R23@123','2024-11-18 08:02:43'),(3,'Ram','Rm32@165','2024-11-18 08:02:43'),(4,'ravi kan','Ravi@48555','2024-11-18 08:02:43'),(5,'Rahul kumar','rk@123','2024-11-17 08:06:33');
/*!40000 ALTER TABLE `emp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrollments`
--

DROP TABLE IF EXISTS `enrollments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollments` (
  `enrollment_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `course_id` int NOT NULL,
  `enrollment_date` date DEFAULT NULL,
  `admission_id` int DEFAULT NULL,
  PRIMARY KEY (`enrollment_id`),
  KEY `user_id` (`user_id`),
  KEY `course_id` (`course_id`),
  KEY `admission_id` (`admission_id`),
  CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `enrollments_ibfk_3` FOREIGN KEY (`admission_id`) REFERENCES `admissions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollments`
--

LOCK TABLES `enrollments` WRITE;
/*!40000 ALTER TABLE `enrollments` DISABLE KEYS */;
INSERT INTO `enrollments` VALUES (1,161,3,'2025-01-18',133),(2,161,4,'2025-01-18',134),(3,161,5,'2025-01-18',135),(4,161,2,'2025-01-18',136);
/*!40000 ALTER TABLE `enrollments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `installments`
--

DROP TABLE IF EXISTS `installments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `installments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `admission_id` int NOT NULL,
  `user_id` int NOT NULL,
  `course_id` int NOT NULL,
  `installment_number` int NOT NULL,
  `installment_amount` decimal(10,2) NOT NULL,
  `due_date` date NOT NULL,
  `paid` tinyint NOT NULL DEFAULT '0',
  `payment_date` timestamp NULL DEFAULT NULL,
  `advance` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `installments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `installments_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `installments`
--

LOCK TABLES `installments` WRITE;
/*!40000 ALTER TABLE `installments` DISABLE KEYS */;
INSERT INTO `installments` VALUES (1,136,161,2,1,500.00,'2025-02-01',0,NULL,0),(2,136,161,2,2,500.00,'2025-03-01',0,NULL,0);
/*!40000 ALTER TABLE `installments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_number` varchar(20) NOT NULL,
  `user_id` int NOT NULL,
  `admission_id` int NOT NULL,
  `amount_paid` decimal(10,2) NOT NULL,
  `payment_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `payment_method` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `bill_number` (`bill_number`),
  KEY `user_id` (`user_id`),
  KEY `admission_id` (`admission_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`admission_id`) REFERENCES `admissions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,'BIL202501185126',161,133,1000.00,'2025-01-17 18:30:00',NULL),(2,'BIL202501185298',161,134,1000.00,'2025-01-17 18:30:00',NULL),(3,'BIL202501181577',161,135,5000.00,'2025-01-17 18:30:00',NULL),(4,'BIL202501185362',161,136,1000.00,'2025-01-17 18:30:00',NULL);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `contact` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `email_2` (`email`),
  UNIQUE KEY `email_3` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (161,'Pulkit',23,'8327323','p@gmail.com',NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-18 15:47:37
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: pulkit
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `Username` varchar(1000) DEFAULT NULL,
  `Password` varchar(1000) DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `email_2` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('Raj Kumar','Raj@123',1,NULL),('Rahul Singh','Rahul@123',2,NULL),('Raju sharma','Raju@123',3,NULL),('Manish','Manish@123',4,'Manish@gmail.com'),('rax','Rax@123',5,'rex@gmail.com'),('Ram','Ram@123',6,'Ram@gmail.com');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admissions`
--

DROP TABLE IF EXISTS `admissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admissions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `course_id` int DEFAULT NULL,
  `date_of_admission` date DEFAULT NULL,
  `amount_paid` decimal(10,2) DEFAULT NULL,
  `reference_number` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `reference_number` (`reference_number`),
  KEY `fk_user_id` (`user_id`),
  KEY `admissions_ibfk_2` (`course_id`),
  CONSTRAINT `admissions_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admissions`
--

LOCK TABLES `admissions` WRITE;
/*!40000 ALTER TABLE `admissions` DISABLE KEYS */;
INSERT INTO `admissions` VALUES (157,177,1,'2024-12-28',4000.00,'REF202412286105'),(158,177,6,'2024-12-28',18560.00,'REF20241228432'),(160,177,4,'2024-12-28',0.00,'REF202412284502'),(162,177,2,'2024-12-28',0.00,'REF202412283895'),(163,177,2,'2024-12-28',0.00,'REF202412286073'),(164,178,1,'2024-12-28',2000.00,'REF20241228311'),(165,178,2,'2024-12-28',800.00,'REF202412284476'),(166,179,1,'2024-12-28',3166.00,'REF20241228674'),(167,180,1,'2024-12-29',8002.00,'REF202412296763'),(168,181,1,'2024-12-29',8002.00,'REF202412295757'),(169,182,1,'2024-12-29',6189.00,'REF202412298042'),(170,183,1,'2024-12-29',8002.00,'REF202412293098'),(171,184,1,'2024-12-29',8002.00,'REF202412296676'),(172,185,1,'2024-12-29',9812.50,'REF202412293728'),(173,185,2,'2024-12-30',2000.01,'REF202412301204'),(175,185,4,'2024-12-30',30000.00,'REF202412308144'),(176,185,3,'2024-12-30',20000.01,'REF202412307448'),(177,186,1,'2025-01-03',4500.00,'REF20250103658'),(178,187,1,'2025-01-13',3333.33,'REF202501135232'),(179,188,1,'2025-01-15',3333.33,'REF202501158681');
/*!40000 ALTER TABLE `admissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `companies`
--

DROP TABLE IF EXISTS `companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companies` (
  `company_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `database_name` varchar(100) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companies`
--

LOCK TABLES `companies` WRITE;
/*!40000 ALTER TABLE `companies` DISABLE KEYS */;
INSERT INTO `companies` VALUES (1,'Company A','pulkit','2024-12-09 20:59:27'),(2,'Company B','company_b_db','2024-12-09 20:59:27'),(3,'Company C','company_c_db','2024-12-09 20:59:27');
/*!40000 ALTER TABLE `companies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_users`
--

DROP TABLE IF EXISTS `company_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company_users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `company_id` int DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `company_id` (`company_id`),
  CONSTRAINT `company_users_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_users`
--

LOCK TABLES `company_users` WRITE;
/*!40000 ALTER TABLE `company_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `company_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(1000) DEFAULT NULL,
  `description` text,
  `fees` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,'JAVA','Java is a versatile programming language commonly used for developing mobile applications, web applications, desktop applications, games, and more. It plays a crucial role in the software development industry, powering various innovations in the digital world. For those seeking to download the Java Development Kit (JDK), OpenJDK has early access builds available for developers.\n\n',8000.00),(2,'React','should know javascript',2000.00),(3,'JAVA+REACT','JAVA is used as a backend and react is used for a front end \n',20000.00),(4,'DATA ANALYSIS','In Data Analysis we have  Power BI ,Tabalue etc.',30000.00),(5,'Cloud Computing ','Cloud computing is a way to access IT resources, like storage, computing power, and databases, over the internet on a pay-as-you-go basis.',45000.00),(6,'Machine Learning','Machine learning is a subset of artificial intelligence (AI) that allows systems to learn and improve from data without being explicitly programmed',90000.00);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emp`
--

DROP TABLE IF EXISTS `emp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emp` (
  `id` int NOT NULL,
  `username` varchar(1000) DEFAULT NULL,
  `password` varchar(1000) DEFAULT NULL,
  `Date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emp`
--

LOCK TABLES `emp` WRITE;
/*!40000 ALTER TABLE `emp` DISABLE KEYS */;
INSERT INTO `emp` VALUES (1,'pulkit','sharma','2024-11-18 08:02:43'),(2,'Raju','R23@123','2024-11-18 08:02:43'),(3,'Ram','Rm32@165','2024-11-18 08:02:43'),(4,'ravi kan','Ravi@48555','2024-11-18 08:02:43'),(5,'Rahul kumar','rk@123','2024-11-17 08:06:33');
/*!40000 ALTER TABLE `emp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrollments`
--

DROP TABLE IF EXISTS `enrollments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollments` (
  `enrollment_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `course_id` int NOT NULL,
  `enrollment_date` date DEFAULT NULL,
  `admission_id` int DEFAULT NULL,
  PRIMARY KEY (`enrollment_id`),
  KEY `user_id` (`user_id`),
  KEY `admission_id` (`admission_id`),
  KEY `enrollments_ibfk_2` (`course_id`),
  CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE,
  CONSTRAINT `enrollments_ibfk_3` FOREIGN KEY (`admission_id`) REFERENCES `admissions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollments`
--

LOCK TABLES `enrollments` WRITE;
/*!40000 ALTER TABLE `enrollments` DISABLE KEYS */;
INSERT INTO `enrollments` VALUES (1,177,1,'2024-12-28',157),(2,177,6,'2024-12-28',158),(4,177,4,'2024-12-28',160),(7,177,2,'2024-12-28',163),(8,178,1,'2024-12-28',164),(9,178,2,'2024-12-28',165),(10,179,1,'2024-12-28',166),(11,180,1,'2024-12-29',167),(12,181,1,'2024-12-29',168),(13,182,1,'2024-12-29',169),(14,183,1,'2024-12-29',170),(15,184,1,'2024-12-29',171),(16,185,1,'2024-12-29',172),(17,185,2,'2024-12-30',173),(19,185,4,'2024-12-30',175),(20,185,3,'2024-12-30',176),(21,186,1,'2025-01-03',177),(22,187,1,'2025-01-13',178),(23,188,1,'2025-01-15',179);
/*!40000 ALTER TABLE `enrollments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `installments`
--

DROP TABLE IF EXISTS `installments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `installments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `admission_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `course_id` int DEFAULT NULL,
  `installment_number` int DEFAULT NULL,
  `installment_amount` decimal(10,2) DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `paid` tinyint(1) DEFAULT '0',
  `payment_date` timestamp NULL DEFAULT NULL,
  `advance` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `admission_id` (`admission_id`),
  KEY `user_id` (`user_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `installments_ibfk_1` FOREIGN KEY (`admission_id`) REFERENCES `admissions` (`id`),
  CONSTRAINT `installments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `installments_ibfk_3` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `installments`
--

LOCK TABLES `installments` WRITE;
/*!40000 ALTER TABLE `installments` DISABLE KEYS */;
INSERT INTO `installments` VALUES (1,157,177,1,1,0.00,'2025-01-01',1,'2024-12-28 16:12:03',0),(2,157,177,1,2,0.00,'2025-02-01',1,'2024-12-28 16:13:02',0),(3,157,177,1,3,1000.00,'2025-03-01',0,NULL,0),(4,157,177,1,4,1000.00,'2025-04-01',0,NULL,0),(5,157,177,1,5,1000.00,'2025-05-01',0,NULL,0),(6,157,177,1,6,1000.00,'2025-06-01',0,NULL,0),(7,158,177,6,1,0.00,'2025-03-01',1,'2024-12-27 19:06:54',0),(8,158,177,6,2,17860.00,'2025-04-01',0,NULL,0),(9,158,177,6,3,17860.00,'2025-05-01',0,NULL,0),(10,158,177,6,4,17860.00,'2025-06-01',0,NULL,0),(11,158,177,6,5,17860.00,'2025-07-01',0,NULL,0),(17,160,177,4,1,6000.00,'2024-12-01',0,NULL,0),(18,160,177,4,2,6000.00,'2025-01-01',0,NULL,0),(19,160,177,4,3,6000.00,'2025-02-01',0,NULL,0),(20,160,177,4,4,6000.00,'2025-03-01',0,NULL,0),(21,160,177,4,5,6000.00,'2025-04-01',0,NULL,0),(22,164,178,1,1,0.00,'2024-12-01',1,'2024-12-28 16:28:57',0),(23,164,178,1,2,0.00,'2025-01-01',1,'2024-12-28 16:29:04',0),(24,164,178,1,3,1000.00,'2025-02-01',0,NULL,0),(25,164,178,1,4,1000.00,'2025-03-01',0,NULL,0),(26,164,178,1,5,1000.00,'2025-04-01',0,NULL,0),(27,164,178,1,6,1000.00,'2025-05-01',0,NULL,0),(28,164,178,1,7,1000.00,'2025-06-01',0,NULL,0),(29,164,178,1,8,1000.00,'2025-07-01',0,NULL,0),(30,165,178,2,1,0.00,'2025-12-01',1,'2024-12-27 19:58:04',0),(31,165,178,2,2,0.00,'2026-01-01',1,'2024-12-27 19:58:11',0),(32,165,178,2,3,400.00,'2026-02-01',0,NULL,0),(33,165,178,2,4,400.00,'2026-03-01',0,NULL,0),(34,165,178,2,5,400.00,'2026-04-01',0,NULL,0),(35,166,179,1,1,0.00,'2025-11-01',1,'2024-12-28 16:16:38',0),(36,166,179,1,2,0.00,'2025-12-01',1,'2024-12-28 16:16:52',0),(37,166,179,1,3,0.00,'2026-01-01',1,'2024-12-28 16:17:29',0),(38,166,179,1,4,0.00,'2026-02-01',1,'2024-12-28 16:25:21',0),(39,166,179,1,5,604.17,'2026-03-01',0,NULL,0),(40,166,179,1,6,604.17,'2026-04-01',0,NULL,0),(41,166,179,1,7,604.17,'2026-05-01',0,NULL,0),(42,166,179,1,8,604.17,'2026-06-01',0,NULL,0),(43,166,179,1,9,604.17,'2026-07-01',0,NULL,0),(44,166,179,1,10,604.17,'2026-08-01',0,NULL,0),(45,166,179,1,11,604.17,'2026-09-01',0,NULL,0),(46,166,179,1,12,604.17,'2026-10-01',0,NULL,0),(47,167,180,1,1,0.00,'2025-02-01',1,'2024-12-29 13:22:12',0),(48,167,180,1,2,0.00,'2025-03-01',1,'2024-12-29 13:35:20',0),(49,167,180,1,3,0.00,'2025-04-01',1,'2024-12-29 13:35:23',0),(50,167,180,1,4,0.00,'2025-05-01',1,'2024-12-29 13:35:25',0),(51,168,181,1,1,0.00,'2025-01-01',1,'2024-12-29 13:54:24',0),(52,168,181,1,2,0.00,'2025-02-01',1,'2024-12-29 13:54:38',0),(53,168,181,1,3,0.00,'2025-03-01',1,'2024-12-29 13:55:25',0),(54,168,181,1,4,0.00,'2025-04-01',1,'2024-12-29 13:55:41',0),(55,169,182,1,1,0.00,'2025-01-01',1,'2024-12-29 14:00:08',0),(56,169,182,1,2,0.00,'2025-02-01',1,'2024-12-29 14:00:11',0),(57,169,182,1,3,0.00,'2025-03-01',1,'2024-12-29 14:00:14',0),(58,169,182,1,4,1812.50,'2025-04-01',0,NULL,0),(59,170,183,1,1,0.00,'2025-01-01',1,'2024-12-29 14:07:13',0),(60,170,183,1,2,0.00,'2025-02-01',1,'2024-12-29 14:07:16',0),(61,170,183,1,3,0.00,'2025-03-01',1,'2024-12-29 14:07:18',0),(62,170,183,1,4,0.00,'2025-04-01',1,'2024-12-29 14:07:20',0),(63,171,184,1,1,0.00,'2025-01-01',1,'2024-12-29 14:23:02',0),(64,171,184,1,2,0.00,'2025-02-01',1,'2024-12-29 14:23:05',0),(65,171,184,1,3,0.00,'2025-03-01',1,'2024-12-29 14:23:08',0),(66,171,184,1,4,0.00,'2025-04-01',1,'2024-12-29 14:23:16',0),(67,173,185,2,1,0.00,'2025-01-01',1,'2024-12-30 06:16:13',0),(68,173,185,2,2,0.00,'2025-02-01',1,'2024-12-30 06:17:00',0),(69,173,185,2,3,0.00,'2025-03-01',1,'2024-12-30 06:17:12',0),(70,175,185,4,1,0.00,'2025-01-01',1,'2024-12-30 06:19:57',0),(71,175,185,4,2,0.00,'2025-02-01',1,'2024-12-30 06:20:06',0),(72,175,185,4,3,0.00,'2025-03-01',1,'2024-12-30 06:20:09',0),(73,176,185,3,1,0.00,'2025-01-01',1,'2024-12-30 07:03:02',0),(74,176,185,3,2,0.00,'2025-02-01',1,'2024-12-30 07:03:12',0),(75,176,185,3,3,0.00,'2025-03-01',1,'2024-12-30 07:03:16',0),(76,177,186,1,1,0.00,'2025-02-01',1,'2025-01-03 08:51:53',0),(77,177,186,1,2,3500.00,'2025-03-01',0,NULL,0),(78,178,187,1,1,0.00,'2025-02-01',1,'2025-01-13 06:32:52',0),(79,178,187,1,2,2333.33,'2025-03-01',0,NULL,0),(80,178,187,1,3,2333.33,'2025-04-01',0,NULL,0),(81,179,188,1,1,0.00,'2025-02-01',1,'2025-01-15 08:11:41',0),(82,179,188,1,2,2333.33,'2025-03-01',0,NULL,0),(83,179,188,1,3,2333.33,'2025-04-01',0,NULL,0);
/*!40000 ALTER TABLE `installments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_number` varchar(20) NOT NULL,
  `user_id` int NOT NULL,
  `admission_id` int NOT NULL,
  `amount_paid` decimal(10,2) NOT NULL,
  `payment_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `payment_method` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `admission_id` (`admission_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`admission_id`) REFERENCES `admissions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,'BIL202412281295',177,157,2000.00,'2024-12-27 18:30:00',NULL),(2,'BIL202412282102',177,158,700.00,'2024-12-27 18:30:00',NULL),(3,'BIL202412282102',177,158,500.00,'2024-12-27 19:06:10',NULL),(4,'BIL202412282102',177,158,17360.00,'2024-12-27 19:06:54',NULL),(6,'BIL202412283390',177,160,0.00,'2024-12-27 18:30:00',NULL),(9,'BIL202412282415',177,163,0.00,'2024-12-27 18:30:00',NULL),(10,'BIL202412288198',178,164,0.00,'2024-12-27 18:30:00',NULL),(11,'BIL202412281497',178,165,0.00,'2024-12-27 18:30:00',NULL),(12,'BIL202412281497',178,165,50.00,'2024-12-27 19:57:40',NULL),(13,'BIL202412281497',178,165,60.00,'2024-12-27 19:57:46',NULL),(14,'BIL202412281497',178,165,289.00,'2024-12-27 19:57:59',NULL),(15,'BIL202412281497',178,165,1.00,'2024-12-27 19:58:04',NULL),(16,'BIL202412281497',178,165,400.00,'2024-12-27 19:58:11',NULL),(17,'BIL202412281295',177,157,1000.00,'2024-12-28 16:12:03',NULL),(18,'BIL202412281295',177,157,50.00,'2024-12-28 16:12:56',NULL),(19,'BIL202412281295',177,157,950.00,'2024-12-28 16:13:02',NULL),(20,'BIL20241228170',179,166,750.00,'2024-12-27 18:30:00',NULL),(21,'BIL20241228170',179,166,604.17,'2024-12-28 16:16:38',NULL),(22,'BIL20241228170',179,166,1200.00,'2024-12-28 16:16:52',NULL),(23,'BIL20241228170',179,166,8.34,'2024-12-28 16:17:29',NULL),(24,'BIL20241228170',179,166,604.17,'2024-12-28 16:25:21',NULL),(25,'BIL202412288198',178,164,1000.00,'2024-12-28 16:28:57',NULL),(26,'BIL202412288198',178,164,1000.00,'2024-12-28 16:29:04',NULL),(27,'BIL202412292034',180,167,750.00,'2024-12-28 18:30:00',NULL),(28,'BIL202412292034',180,167,1812.50,'2024-12-29 13:22:12',NULL),(29,'BIL202412292034',180,167,1812.50,'2024-12-29 13:35:20',NULL),(30,'BIL202412292034',180,167,1812.50,'2024-12-29 13:35:23',NULL),(31,'BIL202412292034',180,167,1812.50,'2024-12-29 13:35:25',NULL),(32,'BIL202412294674',181,168,750.00,'2024-12-28 18:30:00',NULL),(33,'BIL202412294674',181,168,1812.50,'2024-12-29 13:54:24',NULL),(34,'BIL202412294674',181,168,1812.50,'2024-12-29 13:54:38',NULL),(35,'BIL202412294674',181,168,1812.50,'2024-12-29 13:55:25',NULL),(36,'BIL202412294674',181,168,1812.50,'2024-12-29 13:55:41',NULL),(37,'BIL202412294180',182,169,750.00,'2024-12-28 18:30:00',NULL),(38,'BIL202412294180',182,169,1812.50,'2024-12-29 14:00:08',NULL),(39,'BIL202412294180',182,169,1812.50,'2024-12-29 14:00:11',NULL),(40,'BIL202412294180',182,169,1812.50,'2024-12-29 14:00:14',NULL),(41,'BIL202412291802',183,170,750.00,'2024-12-28 18:30:00',NULL),(42,'BIL202412291802',183,170,1812.50,'2024-12-29 14:07:13',NULL),(43,'BIL202412291802',183,170,1812.50,'2024-12-29 14:07:16',NULL),(44,'BIL202412291802',183,170,1812.50,'2024-12-29 14:07:18',NULL),(45,'BIL202412291802',183,170,1812.50,'2024-12-29 14:07:20',NULL),(46,'BIL202412298240',184,171,750.00,'2024-12-28 18:30:00',NULL),(47,'BIL202412298240',184,171,1812.50,'2024-12-29 14:23:02',NULL),(48,'BIL202412298240',184,171,1812.50,'2024-12-29 14:23:05',NULL),(49,'BIL202412298240',184,171,1812.50,'2024-12-29 14:23:08',NULL),(50,'BIL202412298240',184,171,1812.50,'2024-12-29 14:23:16',NULL),(51,'BIL202412297019',185,172,750.00,'2024-12-28 18:30:00',NULL),(52,'BIL202412297019',185,172,1812.50,'2024-12-29 14:29:14',NULL),(53,'BIL202412297019',185,172,1812.50,'2024-12-29 14:29:16',NULL),(54,'BIL202412297019',185,172,1812.50,'2024-12-29 14:29:18',NULL),(55,'BIL202412297019',185,172,1812.50,'2024-12-29 14:29:21',NULL),(56,'BIL202412297019',185,172,1812.50,'2024-12-29 14:29:25',NULL),(57,'BIL202412303158',185,173,750.00,'2024-12-29 18:30:00',NULL),(58,'BIL202412303158',185,173,416.67,'2024-12-30 06:16:13',NULL),(59,'BIL202412303158',185,173,11.00,'2024-12-30 06:16:21',NULL),(60,'BIL202412303158',185,173,11.00,'2024-12-30 06:16:26',NULL),(61,'BIL202412303158',185,173,11.00,'2024-12-30 06:16:43',NULL),(62,'BIL202412303158',185,173,11.00,'2024-12-30 06:16:47',NULL),(63,'BIL202412303158',185,173,11.00,'2024-12-30 06:16:51',NULL),(64,'BIL202412303158',185,173,361.67,'2024-12-30 06:17:00',NULL),(65,'BIL202412303158',185,173,416.67,'2024-12-30 06:17:12',NULL),(67,'BIL20241230163',185,175,750.00,'2024-12-29 18:30:00',NULL),(68,'BIL20241230163',185,175,100.00,'2024-12-30 06:19:48',NULL),(69,'BIL20241230163',185,175,9650.00,'2024-12-30 06:19:57',NULL),(70,'BIL20241230163',185,175,9750.00,'2024-12-30 06:20:06',NULL),(71,'BIL20241230163',185,175,9750.00,'2024-12-30 06:20:09',NULL),(77,'BIL20241230944',185,176,0.00,'2024-12-29 18:30:00',NULL),(78,'BIL20241230944',185,176,11.00,'2024-12-30 07:02:48',NULL),(79,'BIL20241230944',185,176,11.00,'2024-12-30 07:02:50',NULL),(80,'BIL20241230944',185,176,11.00,'2024-12-30 07:02:52',NULL),(81,'BIL20241230944',185,176,11.00,'2024-12-30 07:02:54',NULL),(82,'BIL20241230944',185,176,6622.67,'2024-12-30 07:03:02',NULL),(83,'BIL20241230944',185,176,6666.67,'2024-12-30 07:03:12',NULL),(84,'BIL20241230944',185,176,6666.67,'2024-12-30 07:03:16',NULL),(85,'BIL202501033211',186,177,1000.00,'2025-01-02 18:30:00',NULL),(86,'BIL202501033211',186,177,3500.00,'2025-01-03 08:51:53',NULL),(87,'BIL202501134721',187,178,1000.00,'2025-01-12 18:30:00',NULL),(88,'BIL202501134721',187,178,2333.33,'2025-01-13 06:32:52',NULL),(89,'BIL202501158861',188,179,1000.00,'2025-01-14 18:30:00',NULL),(90,'BIL202501158861',188,179,2333.33,'2025-01-15 08:11:41',NULL);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `contact` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `email_2` (`email`),
  UNIQUE KEY `email_3` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=189 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (177,'Pulkit',23,'8920345220','pulkit@gmail.com',NULL,NULL),(178,'raj',77,'231331223','raj@gmail',NULL,NULL),(179,'shitij',20,'982387362368','shitij@gmail.com',NULL,NULL),(180,'vinod sharma ',55,'9650433660','v@gmail.com',NULL,NULL),(181,'Rahul',88,'43434234','rahul@gmail.com',NULL,NULL),(182,'manik',33,'646653','manik@gmail.com',NULL,NULL),(183,'Gaurav',12,'63635653','g@gmail.com',NULL,NULL),(184,'laxmi',23,'892034522','lax@gmail.com',NULL,NULL),(185,'laxman',23,'892034520','laxman@gmail.com',NULL,NULL),(186,'Aryaman',18,'837363511','aryaman@gmail.com',NULL,NULL),(187,'yash',20,'32434','yash@gmail.com',NULL,NULL),(188,'abhay ',20,'97682731','abhay@gmail.com',NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-18 15:47:37
