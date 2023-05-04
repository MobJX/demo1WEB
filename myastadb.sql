-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: myastadb
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `auc_item`
--

DROP TABLE IF EXISTS `auc_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auc_item` (
  `aucID` int NOT NULL,
  `itemID` int NOT NULL,
  PRIMARY KEY (`aucID`,`itemID`),
  KEY `itemID` (`itemID`),
  CONSTRAINT `auc_item_ibfk_1` FOREIGN KEY (`itemID`) REFERENCES `item` (`itemID`),
  CONSTRAINT `auc_item_ibfk_2` FOREIGN KEY (`aucID`) REFERENCES `auction` (`aucID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auc_item`
--

LOCK TABLES `auc_item` WRITE;
/*!40000 ALTER TABLE `auc_item` DISABLE KEYS */;
INSERT INTO `auc_item` VALUES (1,1),(2,2),(3,3);
/*!40000 ALTER TABLE `auc_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auction`
--

DROP TABLE IF EXISTS `auction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auction` (
  `aucID` int NOT NULL AUTO_INCREMENT,
  `ownerID` int NOT NULL,
  `aucPrice` float(10,2) NOT NULL,
  `minUP` float(10,2) NOT NULL,
  `expiryDateTime` datetime NOT NULL,
  `isClosed` tinyint(1) NOT NULL,
  PRIMARY KEY (`aucID`),
  KEY `ownerID` (`ownerID`),
  CONSTRAINT `auction_ibfk_1` FOREIGN KEY (`ownerID`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auction`
--

LOCK TABLES `auction` WRITE;
/*!40000 ALTER TABLE `auction` DISABLE KEYS */;
INSERT INTO `auction` VALUES (1,1,1000.00,100.00,'2023-07-27 20:30:00',0),(2,1,200.00,20.00,'2023-05-27 14:30:00',0),(3,2,900.00,50.00,'2023-05-24 14:30:00',1);
/*!40000 ALTER TABLE `auction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `itemID` int NOT NULL AUTO_INCREMENT,
  `itemName` varchar(45) NOT NULL,
  `description` text,
  `itemPrice` float(10,2) NOT NULL,
  `isSold` tinyint(1) NOT NULL,
  `imagePath` varchar(255) NOT NULL,
  PRIMARY KEY (`itemID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (1,'iphone14 promax','ottimi condizioni',1000.00,0,'dont know'),(2,'airPods pro','ottimi condizioni',200.00,0,'dont know'),(3,'matebook14','good condition',900.00,1,'dont know');
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `offer`
--

DROP TABLE IF EXISTS `offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `offer` (
  `offerID` int NOT NULL AUTO_INCREMENT,
  `userID` int NOT NULL,
  `aucID` int NOT NULL,
  `offerPrice` float(10,2) NOT NULL,
  `offerDayTime` datetime NOT NULL,
  PRIMARY KEY (`offerID`),
  KEY `userID` (`userID`),
  KEY `aucID` (`aucID`),
  CONSTRAINT `offer_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`),
  CONSTRAINT `offer_ibfk_2` FOREIGN KEY (`aucID`) REFERENCES `auction` (`aucID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offer`
--

LOCK TABLES `offer` WRITE;
/*!40000 ALTER TABLE `offer` DISABLE KEYS */;
INSERT INTO `offer` VALUES (1,2,1,1100.00,'2023-04-27 15:30:00'),(8,1,1,1300.00,'2023-04-30 16:44:33'),(9,1,2,400.00,'2023-04-30 16:44:45'),(10,1,2,600.00,'2023-04-30 16:58:37'),(11,1,2,800.00,'2023-04-30 17:01:24'),(12,1,1,1500.00,'2023-04-30 17:02:17'),(13,1,2,1000.00,'2023-04-30 17:02:27'),(14,1,3,1000.00,'2023-04-30 18:02:27');
/*!40000 ALTER TABLE `offer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userID` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'matteo','0811','via del perugino'),(2,'simona','0528','via duomo 1');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-04 22:21:39
