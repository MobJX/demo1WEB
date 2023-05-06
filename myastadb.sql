-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: myastadb
-- ------------------------------------------------------
-- Server version	8.0.33

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
  CONSTRAINT `auc_item_ibfk_1` FOREIGN KEY (`itemID`) REFERENCES `item` (`itemID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `auc_item_ibfk_2` FOREIGN KEY (`aucID`) REFERENCES `auction` (`aucID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auc_item`
--

LOCK TABLES `auc_item` WRITE;
/*!40000 ALTER TABLE `auc_item` DISABLE KEYS */;
INSERT INTO `auc_item` VALUES (1,1),(2,2),(3,3),(4,4),(6,5),(1,6),(5,15),(6,16),(18,17),(17,18),(18,19),(19,22),(19,23);
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auction`
--

LOCK TABLES `auction` WRITE;
/*!40000 ALTER TABLE `auction` DISABLE KEYS */;
INSERT INTO `auction` VALUES (1,1,1000.00,100.00,'2023-07-27 20:30:00',0),(2,1,200.00,20.00,'2023-05-27 14:30:00',0),(3,2,900.00,50.00,'2023-05-24 14:30:00',1),(4,2,2599.00,20.00,'2023-05-28 17:43:00',0),(5,2,849.99,60.00,'2023-05-06 00:09:00',1),(6,2,1649.99,50.00,'2023-05-09 00:38:00',0),(17,2,1199.99,60.00,'2023-05-06 01:21:00',0),(18,2,704.00,30.00,'2023-05-07 00:31:00',0),(19,2,500.00,50.00,'2023-12-25 00:00:00',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (1,'Iphone14 Promax','ottimi condizioni',1000.00,0,'https://cdn.dxomark.com/wp-content/uploads/medias/post-125428/Apple-iPhone-14-Pro-Max_FINAL_featured-image-packshot-review-1.jpg'),(2,'AirPods Pro','ottimi condizioni',200.00,0,'https://m.media-amazon.com/images/I/71zny7BTRlL._AC_SX522_.jpg'),(3,'Matebook14','good condition',900.00,1,'https://m.media-amazon.com/images/I/517g9MKFW+S._AC_SS450_.jpg'),(4,'Alienware m18','good condition',2599.00,0,'https://i.dell.com/sites/csimages/Product_Imagery/all/prod-2585-notebook-alienware-m18-ff-800x550.png'),(5,'iPad Pro 12.9','Very good condition',1299.99,0,'https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_98708055/fee_786_587_png'),(6,'Nokia X30 5G','good condition',350.00,0,'https://www.inside-digital.de/img/X30.png'),(15,'SAMSUNG Galaxy S22','good condition',499.99,0,'https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_96461586/fee_786_587_png'),(16,'iPhone 12','ottimi condition',350.00,0,'https://d1eh9yux7w8iql.cloudfront.net/product_images/None_59a7092b-f2c4-40b9-9000-7987ecc6cd26.jpg'),(17,'Bag','ottimi condition',15.00,0,'https://i.imgur.com/ZNH22BT.jpeg'),(18,'HONOR Magic5 Pro','good condition',1199.99,0,'https://img02.honorfile.com/eu/it/honor/pms/product/6936520822584/428_428_F2647CAEEA7236E9D4F2BA4CBA2AC74264D747BCD6BFBD92mp.png'),(19,'APPLE IPHONE 13','ottimi condition',689.00,0,'https://www.teknozone.it/img/catalogo/13%20PINK.jpg'),(22,'Sara doll','Very useful',250.00,0,'https://m.media-amazon.com/images/I/61rv4XKyYEL._AC_SX425_.jpg'),(23,'Cristian doll','Very useful',250.00,0,'https://pbs.twimg.com/media/DU9jZHNW0AA1Nmk.jpg');
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
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offer`
--

LOCK TABLES `offer` WRITE;
/*!40000 ALTER TABLE `offer` DISABLE KEYS */;
INSERT INTO `offer` VALUES (1,2,1,1100.00,'2023-04-27 15:30:00'),(8,1,1,1300.00,'2023-04-30 16:44:33'),(9,1,2,400.00,'2023-04-30 16:44:45'),(10,1,2,600.00,'2023-04-30 16:58:37'),(11,1,2,800.00,'2023-04-30 17:01:24'),(12,1,1,1500.00,'2023-04-30 17:02:17'),(13,1,2,1000.00,'2023-04-30 17:02:27'),(14,1,3,1000.00,'2023-04-30 18:02:27'),(19,2,4,2800.00,'2023-05-06 23:27:31'),(37,2,6,1200.00,'2023-05-07 00:08:39'),(38,2,18,800.00,'2023-05-07 00:28:22'),(39,2,19,588.00,'2023-05-07 00:45:59');
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

-- Dump completed on 2023-05-07  0:50:48
