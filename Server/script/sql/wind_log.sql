-- MySQL dump 10.13  Distrib 5.5.25a, for Win64 (x86)
--
-- Host: 139.196.21.111    Database: wind_log_001
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
-- Current Database: `wind_log_001`
--

/*!40000 DROP DATABASE IF EXISTS `wind_log_001`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `wind_log_001` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `wind_log_001`;

--
-- Table structure for table `auth_token`
--

DROP TABLE IF EXISTS `auth_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_token` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL,
  `player_id` int(11) DEFAULT NULL,
  `area_id` int(11) DEFAULT NULL,
  `ip` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `index1` (`time`),
  KEY `index3` (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=130913 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL,
  `player_id` int(11) DEFAULT NULL,
  `event_id` int(11) DEFAULT NULL,
  `vid` int(11) DEFAULT NULL,
  `vafter` int(11) DEFAULT NULL,
  `vbefore` int(11) DEFAULT NULL,
  `vchange` int(11) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `index1` (`time`),
  KEY `index3` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=255230 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `login`
--

DROP TABLE IF EXISTS `login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `login` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL,
  `player_id` int(11) DEFAULT NULL,
  `area_id` int(11) DEFAULT NULL,
  `ip` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `index1` (`time`),
  KEY `index3` (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=141703 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `online_status`
--

DROP TABLE IF EXISTS `online_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `online_status` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `area_id` int(11) DEFAULT '0',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `max` int(11) DEFAULT '0' COMMENT '在线玩家最大值',
  `min` int(11) DEFAULT '0' COMMENT '在线玩家最小值',
  `avg` int(11) DEFAULT '0' COMMENT '在线玩家平均值',
  `online_max` int(11) DEFAULT '0',
  `offline_max` int(11) DEFAULT '0',
  `cache_max` int(11) DEFAULT '0',
  `online_num` int(11) DEFAULT NULL,
  `offline_num` int(11) DEFAULT NULL,
  `cache_num` int(11) DEFAULT NULL,
  `session_size` int(11) DEFAULT NULL,
  `token_size` int(11) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `index1` (`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `server_statistics`
--

DROP TABLE IF EXISTS `server_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `server_statistics` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `area_id` int(11) DEFAULT '0',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `online_player_count` int(11) DEFAULT NULL,
  `offline_player_count` int(11) DEFAULT NULL,
  `global_player_count` int(11) DEFAULT NULL,
  `session_count` int(11) DEFAULT NULL,
  `token_count` int(11) DEFAULT NULL,
  `data_total_player_count` int(11) DEFAULT NULL,
  `data_info_player_count` int(11) DEFAULT NULL,
  `data_cache_player_count` int(11) DEFAULT NULL,
  `unwrite_prompt_sql_count` int(11) DEFAULT NULL,
  `unwrite_cache_sql_count` int(11) DEFAULT NULL,
  `memcache_unwrite_count` int(11) DEFAULT NULL,
  `memcache_fail_count` int(11) DEFAULT NULL,
  `memcache_open` int(11) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `index1` (`area_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2500 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `special`
--

DROP TABLE IF EXISTS `special`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `special` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL,
  `player_id` int(11) DEFAULT NULL,
  `event_id` int(11) DEFAULT NULL,
  `vid` int(11) DEFAULT NULL,
  `vafter` bigint(20) DEFAULT NULL,
  `vbefore` bigint(20) DEFAULT NULL,
  `vchange` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `index1` (`time`),
  KEY `index3` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=255229 DEFAULT CHARSET=utf8;
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
