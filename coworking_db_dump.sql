CREATE DATABASE  IF NOT EXISTS `coworking_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `coworking_db`;
-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: coworking_db
-- ------------------------------------------------------
-- Server version	8.0.30

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
-- Table structure for table `place`
--

DROP TABLE IF EXISTS `place`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `place` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_room` int NOT NULL,
  `area` decimal(5,1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `R_2` (`id_room`),
  CONSTRAINT `place_ibfk_1` FOREIGN KEY (`id_room`) REFERENCES `room` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `place`
--

LOCK TABLES `place` WRITE;
/*!40000 ALTER TABLE `place` DISABLE KEYS */;
INSERT INTO `place` VALUES (1,1,2.0),(2,1,2.0),(3,1,2.0),(4,1,2.0),(5,1,2.0),(6,1,2.0),(7,1,2.0),(8,1,2.0),(9,1,2.0),(10,1,2.0),(11,1,2.0),(12,1,2.0),(13,1,2.0),(14,1,2.0),(15,1,2.0),(16,1,2.0),(17,1,2.0),(18,1,2.0),(19,1,2.0),(20,1,2.0),(21,2,2.0),(22,2,2.0),(23,2,2.0),(24,2,2.0),(25,2,2.0),(26,2,2.0),(27,2,2.0),(28,2,2.0),(29,2,2.0),(30,2,2.0),(31,3,10.0),(32,4,7.0),(33,5,4.0),(34,5,4.0),(35,6,5.0),(36,6,5.0),(37,6,5.0),(38,6,5.0),(39,7,3.0),(40,8,2.5),(41,9,3.0),(42,10,2.0),(43,10,2.0),(44,10,2.0),(45,10,2.0),(46,10,2.0),(47,10,2.0),(48,10,2.0),(49,10,2.0),(50,10,2.0),(51,10,2.0),(52,10,2.0),(53,10,2.0),(54,10,2.0);
/*!40000 ALTER TABLE `place` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rent_application`
--

DROP TABLE IF EXISTS `rent_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rent_application` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_date` timestamp NOT NULL,
  `last_change` timestamp NOT NULL,
  `lease_agreement` varchar(12) DEFAULT NULL,
  `id_status` int NOT NULL,
  `id_user` int NOT NULL,
  `rent_amount` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `R_22` (`id_user`),
  KEY `rent_application_ibfk_2` (`id_status`),
  CONSTRAINT `rent_application_ibfk_2` FOREIGN KEY (`id_status`) REFERENCES `status` (`id`) ON UPDATE RESTRICT,
  CONSTRAINT `rent_application_ibfk_3` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rent_application`
--

LOCK TABLES `rent_application` WRITE;
/*!40000 ALTER TABLE `rent_application` DISABLE KEYS */;
INSERT INTO `rent_application` VALUES (1,'2022-11-10 13:12:00','2023-01-06 00:52:26','-',5,4,3000.00),(2,'2022-11-13 10:14:56','2022-12-05 14:06:33','-',2,6,5000.00),(3,'2022-11-13 12:45:59','2022-12-05 14:06:33','-',3,4,6000.00),(4,'2022-11-15 08:41:08','2022-12-05 14:06:33','-',4,7,750.00),(5,'2022-11-18 07:45:23','2022-12-05 14:09:28','-',5,4,720.00),(75,'2023-01-05 23:02:19','2023-01-06 01:02:19',' ',1,7,1500.00);
/*!40000 ALTER TABLE `rent_application` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `SetDatesBeforeInsert` BEFORE INSERT ON `rent_application` FOR EACH ROW BEGIN
set new.rent_amount = 0;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `SetDatesBeforeUpdate` BEFORE UPDATE ON `rent_application` FOR EACH ROW BEGIN
 set new.last_change = now();
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `rent_place`
--

DROP TABLE IF EXISTS `rent_place`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rent_place` (
  `place_id` int NOT NULL,
  `rent_application_id` int NOT NULL,
  `rent_start` timestamp NOT NULL,
  `rent_end` timestamp NOT NULL,
  `rent_amount` decimal(10,2) NOT NULL,
  `tariff_id` int NOT NULL,
  PRIMARY KEY (`place_id`,`rent_application_id`),
  KEY `fk_rent_place_rent_application1_idx` (`rent_application_id`),
  KEY `fk_rent_place_tariff1_idx` (`tariff_id`),
  CONSTRAINT `fk_rent_place_place1` FOREIGN KEY (`place_id`) REFERENCES `place` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_rent_place_rent_application1` FOREIGN KEY (`rent_application_id`) REFERENCES `rent_application` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_rent_place_tariff1` FOREIGN KEY (`tariff_id`) REFERENCES `tariff` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rent_place`
--

LOCK TABLES `rent_place` WRITE;
/*!40000 ALTER TABLE `rent_place` DISABLE KEYS */;
INSERT INTO `rent_place` VALUES (1,1,'2022-12-04 22:00:00','2022-12-11 22:00:00',2000.00,2),(4,4,'2022-11-24 22:00:00','2022-11-26 22:00:00',750.00,2),(5,2,'2022-11-29 22:00:00','2022-12-29 22:00:00',5000.00,3),(12,75,'2022-12-19 20:00:00','2022-12-24 20:00:00',1500.00,2),(31,1,'2022-12-04 22:00:00','2022-12-05 22:00:00',1000.00,5),(36,3,'2022-12-18 22:00:00','2022-12-28 22:00:00',6000.00,10),(45,5,'2022-12-04 22:00:00','2022-12-09 22:00:00',720.00,11);
/*!40000 ALTER TABLE `rent_place` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `CountRentAmountBeforeInsert` BEFORE INSERT ON `rent_place` FOR EACH ROW BEGIN
if(new.rent_amount is null) then
set new.rent_amount = getTimeUnitCount(new.rent_start, new.rent_end, 
  				(select duration from tariff join time_unit on tariff.id_time_unit = time_unit.id where tariff.id = new.tariff_id))*
                  (select price from tariff where tariff.id = new.tariff_id);
                  end if;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `CountRentAmountAfterInsert` AFTER INSERT ON `rent_place` FOR EACH ROW BEGIN
declare counted_amount INT;
update rent_application set rent_amount = rent_amount + new.rent_amount WHERE rent_application.id = new.rent_application_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `rent_place_AFTER_DELETE` AFTER DELETE ON `rent_place` FOR EACH ROW BEGIN
update rent_application set rent_amount = rent_amount - old.rent_amount WHERE rent_application.id = old.rent_application_id;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_unique` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (2,'ROLE_ADMIN'),(1,'ROLE_USER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `area` decimal(5,1) NOT NULL DEFAULT '0.0',
  `max_places` int NOT NULL DEFAULT '0',
  `id_room_type` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `R_1` (`id_room_type`),
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`id_room_type`) REFERENCES `room_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'Amsterdam',50.0,20,1),(2,'Paris',25.0,10,1),(3,'Kyiv',10.0,1,2),(4,'New York',7.0,1,2),(5,'Las Vegas',9.0,2,3),(6,'Tokyo',20.0,4,3),(7,'Rome',3.0,1,4),(8,'Berlin',2.5,1,4),(9,'London',3.0,1,4),(10,'Wellington',30.0,13,5);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_type`
--

DROP TABLE IF EXISTS `room_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `image` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `XAK1room_type` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_type`
--

LOCK TABLES `room_type` WRITE;
/*!40000 ALTER TABLE `room_type` DISABLE KEYS */;
INSERT INTO `room_type` VALUES (1,'open space','Free space in the common workspace','/assets/rooms/open_space.jpeg'),(2,'meeting room','The meeting room is equipped with everything necessary for holding meetings','/assets/rooms/meeting_room.jpeg'),(3,'private office','Secure space for small teams','/assets/rooms/private_office.jpeg'),(4,'skype room','A separate Skype zone for the most important calls','/assets/rooms/skype_room.jpg'),(5,'lounge space','An informal working atmosphere for comfortable work and inspiration','/assets/rooms/lounge_space.jpg');
/*!40000 ALTER TABLE `room_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `XAK1service` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` VALUES (16,''),(19,' service1'),(6,'2 hours in meeting room'),(12,'Access to the kitchen'),(3,'Access to the rest area'),(5,'Fast Wi-Fi connection'),(1,'Fixed workplace'),(10,'Flipchart'),(8,'Free parking'),(4,'Guest visit'),(9,'Individual locker'),(13,'Magnetic cards for entry'),(14,'Necessary technical equipment'),(11,'Printer, copier, scanner'),(21,'service1'),(20,'service2'),(7,'Technical support services'),(2,'Unfixed workplace'),(15,'Unlimited meeting room');
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `XAK1status` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (4,'Cancelled'),(2,'Confirmed'),(1,'New'),(5,'Paid'),(3,'Rejected');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tariff`
--

DROP TABLE IF EXISTS `tariff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tariff` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `id_time_unit` int NOT NULL,
  `id_room_type` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `XAK1tariff` (`name`),
  KEY `R_5` (`id_time_unit`),
  KEY `R_23` (`id_room_type`),
  CONSTRAINT `tariff_ibfk_1` FOREIGN KEY (`id_time_unit`) REFERENCES `time_unit` (`id`),
  CONSTRAINT `tariff_ibfk_2` FOREIGN KEY (`id_room_type`) REFERENCES `room_type` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tariff`
--

LOCK TABLES `tariff` WRITE;
/*!40000 ALTER TABLE `tariff` DISABLE KEYS */;
INSERT INTO `tariff` VALUES (2,'Open Space - Day',2,1,250.00),(3,'Open Space - Month',4,1,2500.00),(5,'Meeting Room - Day',2,2,500.00),(6,'Skype Room - Day',2,4,750.00),(7,'Private office - Month',4,3,7000.00),(8,'Private office - Year',5,3,50000.00),(9,'Open Space - Week',3,1,1200.00),(10,'Private Office - Week',3,3,3000.00),(11,'Lounge Space - Day',2,5,120.00);
/*!40000 ALTER TABLE `tariff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tariff_has_service`
--

DROP TABLE IF EXISTS `tariff_has_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tariff_has_service` (
  `id_tariff` int NOT NULL,
  `id_service` int NOT NULL,
  PRIMARY KEY (`id_tariff`,`id_service`),
  KEY `R_4` (`id_service`),
  CONSTRAINT `tariff_has_service_ibfk_1` FOREIGN KEY (`id_tariff`) REFERENCES `tariff` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tariff_has_service_ibfk_2` FOREIGN KEY (`id_service`) REFERENCES `service` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tariff_has_service`
--

LOCK TABLES `tariff_has_service` WRITE;
/*!40000 ALTER TABLE `tariff_has_service` DISABLE KEYS */;
INSERT INTO `tariff_has_service` VALUES (3,1),(7,1),(8,1),(10,1),(2,2),(9,2),(11,2),(2,3),(3,3),(5,3),(7,3),(8,3),(9,3),(10,3),(11,3),(8,4),(10,4),(2,5),(3,5),(5,5),(6,5),(7,5),(8,5),(9,5),(10,5),(11,5),(7,6),(10,6),(3,7),(5,7),(6,7),(9,7),(10,7),(8,8),(7,9),(8,9),(5,10),(5,11),(7,11),(8,11),(3,12),(7,12),(8,12),(10,12),(8,13),(5,14),(6,14),(8,15);
/*!40000 ALTER TABLE `tariff_has_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `time_unit`
--

DROP TABLE IF EXISTS `time_unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `time_unit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `duration` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `XAK1time_unit` (`duration`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time_unit`
--

LOCK TABLES `time_unit` WRITE;
/*!40000 ALTER TABLE `time_unit` DISABLE KEYS */;
INSERT INTO `time_unit` VALUES (2,'day'),(1,'hour'),(4,'month'),(3,'week'),(5,'year');
/*!40000 ALTER TABLE `time_unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(30) NOT NULL,
  `password` varchar(100) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `passport_id` varchar(45) NOT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  `id_role` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `R_9` (`id_role`),
  FULLTEXT KEY `full_name` (`last_name`,`first_name`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`id_role`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1258794 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin1@admin.com','$2a$08$zQNqdQmZg5jipgscVs4UoO8iHgQksVgqgUc89sJk2KC.cwNFqxh2G','Smith','John','612484564','0564856321',2),(2,'admin2@admin.com','$2a$08$AN2jmXvlbpXK/ugiVvHQvOZ6OdomZPsjr146A47/e5vDuXPkHyPea','Graham','Alicia','612484568','0369452684',2),(3,'admin3@admin.com','$2a$08$h1tQcsQgq7Lg6jZs/MaZ7OYoORNU8jyuqty14wUs1eKX/jRZ7jyNO','Williamson','Alicia','612489425','0789456328',2),(4,'test@gmail.com','$2a$04$ihVvT8g91ZgGXCuuVgCsEeeT0X3/sqIR.JaZii6F0Q/An/A3qs5Om','Mullins','Sally','636889425','069256378',1),(5,'rfisher@outlook.com','$2a$04$hwfPOWBqHRcZ9Ed/2pPX1OrkqDmgf/3hCuuYWUH3DLQh2XHaMhI.a','Shelton','Roxanne','612125425','065844658',1),(6,'konst@hotmail.com','$2a$04$0XEkXNByagP.9F0NPmcZwuMuQ0AEt0FV6CWZ46UlqQHZcmPhBGQ6.','Rogers','Herbert','697489425','105955965',1),(7,'daveewart@live.com','$2a$04$1KkGuZjof7xFB2OHselFZ.xiWy2H5KQsxSvpM3oVmYSSrr4C2Yapq','Mccoy','Todd','612436925','0896324691',1),(8,'user@user.com','user','Smith','John','649244812','0984665816',1),(9,'admin@admin.com','admin','Smith','Tom','265954466','0658426484',2);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'coworking_db'
--
/*!50003 DROP FUNCTION IF EXISTS `getTimeUnitCount` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `getTimeUnitCount`(_start timestamp, _end timestamp, _time_unit varchar(30)) RETURNS int
    READS SQL DATA
    DETERMINISTIC
BEGIN
RETURN case lower(_time_unit)
		when 'hour' then hour(timediff(_start,_end))
        when 'day' then timestampdiff(day,_start,_end) + 1
        when 'week' then timestampdiff(week,_start,_end) + 1
        when 'month' then timestampdiff(month,_start,_end) + 1
        when 'year' then timestampdiff(year,_start,_end) + 1
        else 0
	end;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `AvailablePlace` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AvailablePlace`( IN _date_from TIMESTAMP, IN _date_to TIMESTAMP, IN _id_room_type INT)
BEGIN
SELECT place.id, place.area, room.id, room.name, room_type.name
FROM room JOIN room_type ON id_room_type = room_type.id
JOIN place ON room.id = place.id_room
WHERE id_room_type = _id_room_type AND place.id NOT IN
	(SELECT place_id FROM rent_place JOIN rent_application ON rent_application_id = rent_application.id JOIN status ON id_status = status.id
		WHERE status.name NOT IN ('Rejected','Cancelled')
        AND rent_start <= _date_from AND _date_to <= rent_end);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-01-08 13:26:25
