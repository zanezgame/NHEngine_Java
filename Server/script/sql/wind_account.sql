-- MySQL dump 10.13  Distrib 5.5.25a, for Win64 (x86)
--
-- Host: 139.196.21.111    Database: wind_account
-- ------------------------------------------------------
-- Server version	5.1.73-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `wind_account`
--

/*!40000 DROP DATABASE IF EXISTS `wind_account`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `wind_account` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `wind_account`;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `account_idx` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(1024) DEFAULT NULL,
  `pass` varchar(256) DEFAULT NULL,
  `channel_id` int(11) DEFAULT NULL,
  `os_type` varchar(256) DEFAULT NULL,
  `os_version` varchar(256) DEFAULT NULL,
  `device_name` varchar(256) DEFAULT NULL,
  `udid` varchar(256) DEFAULT NULL,
  `lastarea_id` int(11) DEFAULT '-1',
  `create_time` datetime DEFAULT NULL,
  `login_time` datetime DEFAULT NULL,
  PRIMARY KEY (`account_idx`),
  KEY `social_type_id` (`udid`(255)) USING BTREE,
  KEY `device_id` (`account`(255)) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=130715 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_area`
--

DROP TABLE IF EXISTS `account_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_area` (
  `account_idx` int(11) NOT NULL COMMENT '账号编号',
  `area_id` int(11) NOT NULL COMMENT '区编号',
  `area_status` int(11) NOT NULL DEFAULT '1' COMMENT '区状态',
  PRIMARY KEY (`account_idx`,`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_local`
--

DROP TABLE IF EXISTS `account_local`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_local` (
  `account_idx` int(11) NOT NULL COMMENT '账号编号',
  `account` varchar(128) DEFAULT NULL COMMENT '账号名',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `udid` varchar(128) NOT NULL COMMENT '唯一标示',
  PRIMARY KEY (`account_idx`),
  UNIQUE KEY `unique1` (`account`),
  KEY `normal1` (`udid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-10-12 17:01:30
