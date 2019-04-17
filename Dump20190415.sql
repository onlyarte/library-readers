-- MySQL dump 10.13  Distrib 8.0.15, for macos10.14 (x86_64)
--
-- Host: localhost    Database: LibraryTest
-- ------------------------------------------------------
-- Server version	8.0.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


CREATE DATABASE if not exists Library;

use Library;
--
-- Table structure for table `Authors`
--

DROP TABLE IF EXISTS `Authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `Authors` (
  `authorId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`authorId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Authors`
--

LOCK TABLES `Authors` WRITE;
/*!40000 ALTER TABLE `Authors` DISABLE KEYS */;
INSERT INTO `Authors` VALUES (1,'Jim Joe');
/*!40000 ALTER TABLE `Authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Books`
--

DROP TABLE IF EXISTS `Books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `Books` (
  `bookId` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  `size` int(11) NOT NULL,
  `editionId` int(11) NOT NULL,
  PRIMARY KEY (`bookId`),
  KEY `editionId_idx` (`editionId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Books`
--

LOCK TABLES `Books` WRITE;
/*!40000 ALTER TABLE `Books` DISABLE KEYS */;
INSERT INTO `Books` VALUES (5,'підручник',240,5);
/*!40000 ALTER TABLE `Books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Downloads`
--

DROP TABLE IF EXISTS `Downloads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `Downloads` (
  `downloadId` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `readerId` int(11) NOT NULL,
  `editionId` int(11) NOT NULL,
  PRIMARY KEY (`downloadId`),
  KEY `DowloadToReader_idx` (`readerId`),
  KEY `DowloadToEdition_idx` (`editionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Downloads`
--

LOCK TABLES `Downloads` WRITE;
/*!40000 ALTER TABLE `Downloads` DISABLE KEYS */;
/*!40000 ALTER TABLE `Downloads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EditionCopies`
--

DROP TABLE IF EXISTS `EditionCopies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `EditionCopies` (
  `editionCopyId` int(11) NOT NULL AUTO_INCREMENT,
  `editionId` int(11) NOT NULL,
  PRIMARY KEY (`editionCopyId`),
  KEY `editionId_idx` (`editionId`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EditionCopies`
--

LOCK TABLES `EditionCopies` WRITE;
/*!40000 ALTER TABLE `EditionCopies` DISABLE KEYS */;
INSERT INTO `EditionCopies` VALUES (28,5),(29,5),(30,5),(31,5),(32,5),(33,6),(34,6),(35,6);
/*!40000 ALTER TABLE `EditionCopies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Editions`
--

DROP TABLE IF EXISTS `Editions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `Editions` (
  `editionId` int(11) NOT NULL AUTO_INCREMENT,
  `dateOfPublication` date NOT NULL,
  `hasElectronicCopy` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`editionId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Editions`
--

LOCK TABLES `Editions` WRITE;
/*!40000 ALTER TABLE `Editions` DISABLE KEYS */;
INSERT INTO `Editions` VALUES (5,'2016-10-31',0),(6,'1998-01-30',1);
/*!40000 ALTER TABLE `Editions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EditionTopics`
--

DROP TABLE IF EXISTS `EditionTopics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `EditionTopics` (
  `editionTopicId` int(11) NOT NULL AUTO_INCREMENT,
  `editionId` int(11) NOT NULL,
  `topic` varchar(45) NOT NULL,
  PRIMARY KEY (`editionTopicId`),
  KEY `EtToEdition_idx` (`editionId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EditionTopics`
--

LOCK TABLES `EditionTopics` WRITE;
/*!40000 ALTER TABLE `EditionTopics` DISABLE KEYS */;
INSERT INTO `EditionTopics` VALUES (1,5,'Programming');
Insert into `EditionTopics` values(2, 6, 'Programming');
/*!40000 ALTER TABLE `EditionTopics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Orders`
--

DROP TABLE IF EXISTS `Orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `Orders` (
  `orderId` int(11) NOT NULL AUTO_INCREMENT,
  `dateReceived` date NOT NULL,
  `dateCompleted` date DEFAULT NULL,
  `result` varchar(45) NOT NULL,
  `editionCopyId` int(11) NOT NULL,
  `readerId` int(11) NOT NULL,
  PRIMARY KEY (`orderId`),
  KEY `OrderToEditionCopy_idx` (`editionCopyId`),
  KEY `OrderToReader_idx` (`readerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Orders`
--

LOCK TABLES `Orders` WRITE;
/*!40000 ALTER TABLE `Orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `Orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PublicationAuthors`
--

DROP TABLE IF EXISTS `PublicationAuthors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `PublicationAuthors` (
  `publicationAuthorId` int(11) NOT NULL AUTO_INCREMENT,
  `publicationId` int(11) NOT NULL,
  `authorId` int(11) NOT NULL,
  PRIMARY KEY (`publicationAuthorId`),
  KEY `PaToPublication_idx` (`publicationId`),
  KEY `PaToAuthor_idx` (`authorId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PublicationAuthors`
--

LOCK TABLES `PublicationAuthors` WRITE;
/*!40000 ALTER TABLE `PublicationAuthors` DISABLE KEYS */;
INSERT INTO `PublicationAuthors` VALUES (1,1,1);
/*!40000 ALTER TABLE `PublicationAuthors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PublicationKeyWords`
--

DROP TABLE IF EXISTS `PublicationKeyWords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `PublicationKeyWords` (
  `publicationKeyWordId` int(11) NOT NULL AUTO_INCREMENT,
  `publicationId` int(11) NOT NULL,
  `keyWord` varchar(45) NOT NULL,
  PRIMARY KEY (`publicationKeyWordId`),
  KEY `PkwToPublication_idx` (`publicationId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PublicationKeyWords`
--

LOCK TABLES `PublicationKeyWords` WRITE;
/*!40000 ALTER TABLE `PublicationKeyWords` DISABLE KEYS */;
INSERT INTO `PublicationKeyWords` VALUES (1,1,'C++'),(2,1,'Programming');
/*!40000 ALTER TABLE `PublicationKeyWords` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Publications`
--

DROP TABLE IF EXISTS `Publications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `Publications` (
  `publicationId` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `seriesId` int(11) DEFAULT NULL,
  `bookId` int(11) DEFAULT NULL,
  PRIMARY KEY (`publicationId`),
  KEY `seriesId_idx` (`seriesId`),
  KEY `bookId_idx` (`bookId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Publications`
--

LOCK TABLES `Publications` WRITE;
/*!40000 ALTER TABLE `Publications` DISABLE KEYS */;
INSERT INTO `Publications` VALUES (1,'C++ for Beginners',NULL,5),(2,'Part 1: Algorithms',1,NULL),(3,'Part 2: Data Structures',1,NULL);
/*!40000 ALTER TABLE `Publications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Readers`
--

DROP TABLE IF EXISTS `Readers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `Readers` (
  `readerId` int(11) NOT NULL AUTO_INCREMENT,
  `lastName` varchar(45) NOT NULL,
  `phoneNumber` varchar(45) NOT NULL,
  `bonusPoints` int(11) NOT NULL DEFAULT '5',
  PRIMARY KEY (`readerId`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Readers`
--

LOCK TABLES `Readers` WRITE;
/*!40000 ALTER TABLE `Readers` DISABLE KEYS */;
INSERT INTO `Readers` VALUES (15,'Пурій','0687117336',5),(16,'ghh','7868876786',5);
/*!40000 ALTER TABLE `Readers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Readings`
--

DROP TABLE IF EXISTS `Readings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `Readings` (
  `readingId` int(11) NOT NULL AUTO_INCREMENT,
  `readerId` int(11) NOT NULL,
  `editionCopyId` int(11) NOT NULL,
  `dateReceived` date NOT NULL,
  `dateReturned` date DEFAULT NULL,
  PRIMARY KEY (`readingId`),
  KEY `ReadingToReader_idx` (`readerId`),
  KEY `ReadingToEditionCopy_idx` (`editionCopyId`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Readings`
--

LOCK TABLES `Readings` WRITE;
/*!40000 ALTER TABLE `Readings` DISABLE KEYS */;
INSERT INTO `Readings` VALUES (10,15,28,'2017-01-15','2017-02-18'),(11,15,29,'2017-10-25','2018-01-30'),(12,15,28,'2019-01-15',NULL),(13,15,34,'2018-09-30',NULL);
/*!40000 ALTER TABLE `Readings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Series`
--

DROP TABLE IF EXISTS `Series`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `Series` (
  `seriesId` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `numberOfBooks` int(11) NOT NULL,
  `editionId` int(11) NOT NULL,
  PRIMARY KEY (`seriesId`),
  KEY `SeriesToEdition_idx` (`editionId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Series`
--

LOCK TABLES `Series` WRITE;
/*!40000 ALTER TABLE `Series` DISABLE KEYS */;
INSERT INTO `Series` VALUES (1,'Java Intro',2,6);
/*!40000 ALTER TABLE `Series` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Uploads`
--

DROP TABLE IF EXISTS `Uploads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `Uploads` (
  `uploadId` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `readerId` int(11) NOT NULL,
  `editionId` int(11) NOT NULL,
  PRIMARY KEY (`uploadId`),
  KEY `UploadToReader_idx` (`readerId`),
  KEY `UploadToEdition_idx` (`editionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Uploads`
--

LOCK TABLES `Uploads` WRITE;
/*!40000 ALTER TABLE `Uploads` DISABLE KEYS */;
/*!40000 ALTER TABLE `Uploads` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-04-15 22:10:58
