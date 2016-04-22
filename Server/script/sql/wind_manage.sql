-- MySQL dump 10.13  Distrib 5.5.25a, for Win64 (x86)
--
-- Host: 139.196.21.111    Database: wind_manage
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
-- Current Database: `wind_manage`
--

/*!40000 DROP DATABASE IF EXISTS `wind_manage`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `wind_manage` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `wind_manage`;

--
-- Table structure for table `area_info`
--

DROP TABLE IF EXISTS `area_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `area_info` (
  `id` int(11) NOT NULL DEFAULT '0' COMMENT '和clientShowAreaID值相同，为服务器区号',
  `name` varchar(128) DEFAULT '' COMMENT '区名',
  `areaStartTime` datetime DEFAULT '1970-01-01 08:00:00' COMMENT '按照"yyyy-MM-dd HH:mm:ss"',
  `status` int(11) DEFAULT '0' COMMENT '服务器状态： 0-客户端能收到此区信息，只是不显示  1-新区  2-繁忙  3-饱满  4-维护  5-仅白名单ip可见',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_info`
--

DROP TABLE IF EXISTS `base_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_info` (
  `server_type` tinyint(8) NOT NULL DEFAULT '0' COMMENT '充值服务器类型0-官方正式服1-审核服2-内网服',
  `white_ip_open` tinyint(8) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='必须有数据';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `freeze_info`
--

DROP TABLE IF EXISTS `freeze_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `freeze_info` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL DEFAULT '0',
  `show_id` bigint(20) NOT NULL DEFAULT '0',
  `type` int(11) NOT NULL,
  `begin_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `white_ip_info`
--

DROP TABLE IF EXISTS `white_ip_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `white_ip_info` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-10-12 17:01:31
