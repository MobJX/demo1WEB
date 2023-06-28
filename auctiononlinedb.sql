-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: auctiononlinedb
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
INSERT INTO `auc_item` VALUES (4,3),(1,4),(1,5),(2,6),(2,7),(3,8),(5,12),(6,13),(6,14);
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
  CONSTRAINT `auction_ibfk_1` FOREIGN KEY (`ownerID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auction`
--

LOCK TABLES `auction` WRITE;
/*!40000 ALTER TABLE `auction` DISABLE KEYS */;
INSERT INTO `auction` VALUES (1,2,168887.00,1000.00,'2023-06-27 12:00:00',1),(2,1,254350.00,5000.00,'2023-12-31 23:00:00',0),(3,2,2500000.00,5000.00,'2023-07-30 15:58:00',0),(4,2,8888.00,80.00,'2023-06-28 13:13:00',0),(5,2,999.00,150.00,'2023-06-28 07:25:00',0),(6,1,498.00,20.00,'2023-06-27 14:00:00',1);
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
  `creatorID` int NOT NULL,
  PRIMARY KEY (`itemID`),
  KEY `userID_idx` (`creatorID`),
  CONSTRAINT `userID` FOREIGN KEY (`creatorID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--
--
-- LOCK TABLES `item` WRITE;
-- /*!40000 ALTER TABLE `item` DISABLE KEYS */;
-- INSERT INTO `item` VALUES (1,'Eiffel Tower','a wrought-iron lattice tower on the Champ de Mars in Paris, France',9999.99,0,'https://upload.wikimedia.org/wikipedia/commons/thumb/8/85/Tour_Eiffel_Wikimedia_Commons_%28cropped%29.jpg/250px-Tour_Eiffel_Wikimedia_Commons_%28cropped%29.jpg',1),(2,'Hirosue Ryoko photo album','beautiful',99.99,0,'https://s.eslite.dev/upload/product/2682304090000/o/9784087880588-1.jpg',2),(3,'Golden Apple','Very Pretty',8888.00,0,'https://i.imgur.com/eevFN7T.png',2),(4,'David (Michelangelo)','David is a masterpiece of Italian Renaissance sculpture, created from 1501 to 1504 by Michelangelo.',99999.00,0,'https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Michelangelo%27s_David_-_right_view_2.jpg/239px-Michelangelo%27s_David_-_right_view_2.jpg',2),(5,'Venus de Milo','The Venus de Milo is a 2 m tall Parian marble statue of a Greek goddess, most likely Aphrodite, depicted with a bare torso and drapery over the lower half of her body.',68888.00,0,'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c2/Front_views_of_the_Venus_de_Milo.jpg/270px-Front_views_of_the_Venus_de_Milo.jpg',2),(6,'Titanic ticket','Experience Unparalleled Luxury and Grandeur: Titanic\'s Ticket to Unforgettable Voyage',4350.00,0,'https://www.chinanews.com.cn/gj/2012/04-16/U381P4T8D3822772F107DT20120416152223.jpg',1),(7,'Ocean Gate Titan\'s ticket','Experience Unparalleled Luxury and Grandeur: Titanic\'s Ticket to Unforgettable Voyage',250000.00,0,'https://images.wsj.net/im-803632/social',1),(8,'White House','The White House is the official residence and workplace of the president of the United States. It is located at 1600 Pennsylvania Avenue NW in Washington, D.C., and has been the residence of every U.S. president since John Adams in 1800 when the national capital was moved from Philadelphia to Washington, D.C. The term \"White House\" is often used as metonymy for the president and his advisers.',2500000.00,0,'https://www.whitehouse.gov/wp-content/uploads/2021/01/about_the_white_house.jpg',2),(9,'Dragon robe','Dragon robes, also known as gunlongpao or longpao for short, is a form of everyday clothing which had a Chinese dragon, called long as the main decoration; it was worn by the emperors of China',999999.00,0,'https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/Drachenrobe-Qianlong.JPG/100px-Drachenrobe-Qianlong.JPG',1),(10,'Yui Aragaki photo album','Yui Aragaki (Hepburn: Aragaki Yui, born June 11, 1988) is a Japanese actress, model, singer and occasional radio show host.[1][2] She has been selected several times as the most desired girlfriend and the most desired female celebrity face in Oricon\'s yearly survey',189.00,0,'https://m.media-amazon.com/images/I/81n55ihmt8L._AC_UF894,1000_QL80_.jpg',2),(11,'Airi Suzuki photo album','Airi Suzuki (uzuki Airi, born April 12, 1994)[1][2] is a Japanese singer, actress, model, and radio personality associated with Up-Front Works. In 2002, she joined Hello! Project as a member of Hello! Project Kids and later debuted as one of the lead vocalist of the Japanese idol girl group Cute in 2005. ',139.00,0,'https://xxzhiku.com/wp-content/uploads/2022/11/4c2ad30459224336.jpg',2),(12,' 2008 Summer Olympics golden medal','Matteo\'s 100 m champion',999.00,0,'https://photocdn.sohu.com/20070328/Img249037387.jpg',2),(13,'Diamond Pickaxe','A pickaxe is a tool required to mine ores, rocks, rock-based blocks and metal-based blocks quickly and obtain them as items. A pickaxe mines faster and can obtain more block types as items depending on the material it is made from',199.00,0,'https://w7.pngwing.com/pngs/131/337/png-transparent-minecraft-diamond-pickaxe-illustration-minecraft-pocket-edition-pickaxe-mod-mining-angle-text-square.png',1),(14,'Diamond Sword','The sword is a melee weapon that is mainly used to deal damage to entities or for breaking certain blocks faster than by hand. A sword is made from one of six materials, in order of increasing quality and expense: wood, gold, stone, iron, diamond and netherite.',299.00,0,'https://e1.pxfuel.com/desktop-wallpaper/232/551/desktop-wallpaper-minecraft-diamond-sword-transparent-backgrounds-minecraft-craft-diamond-background.jpg',1),(15,'One Ring','One ring to rule them all, one ring to find them, One ring to bring them all and in the darkness bind them.',1973.00,0,'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/One_Ring_Blender_Render.png/125px-One_Ring_Blender_Render.png',1);
-- /*!40000 ALTER TABLE `item` ENABLE KEYS */;
-- UNLOCK TABLES;

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
  CONSTRAINT `offer_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `offer_ibfk_2` FOREIGN KEY (`aucID`) REFERENCES `auction` (`aucID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offer`
--

LOCK TABLES `offer` WRITE;
/*!40000 ALTER TABLE `offer` DISABLE KEYS */;
INSERT INTO `offer` VALUES (1,1,1,200000.00,'2023-06-27 11:53:00'),(2,2,2,300000.00,'2023-06-27 11:59:41'),(3,2,6,800.00,'2023-06-27 13:59:18'),(4,1,3,3000000.00,'2023-06-27 14:00:51'),(5,1,4,10000.00,'2023-06-27 14:01:40'),(6,2,2,350000.00,'2023-06-27 16:30:22');
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
INSERT INTO `user` VALUES (1,'simona','0528','via Duomo 1'),(2,'matteo','0811','via San Pietro 1');
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

-- Dump completed on 2023-06-27 16:41:42
