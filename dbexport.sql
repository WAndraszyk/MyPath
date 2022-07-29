CREATE DATABASE  IF NOT EXISTS `mypath` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `mypath`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: mypath
-- ------------------------------------------------------
-- Server version	5.1.49-community

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
-- Table structure for table `routes`
--

DROP TABLE IF EXISTS `routes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `routes` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL DEFAULT '',
  `way` varchar(1000) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `routes`
--

LOCK TABLES `routes` WRITE;
/*!40000 ALTER TABLE `routes` DISABLE KEYS */;
INSERT INTO `routes` VALUES (0,'Bicycle Ring of the Poznan Powiat','The bicycle route around Poznan with seven connecting routes leading to it from the city center was conceived as a network of tourist routes. The route passes through interesting regions (Wielkopolski National Park, Puszcza Zielonka Landscape Park, Promno Landscape Park, Rogalinski Landscape Park and the most famous towns in the vicinity of the capital of Wielkopolska). The trail goes through Kornik, Rogalin, Steszew, Biedrusko, and Kostrzyn. The route runs through the military training ground in Biedrusko, access to which is possible after obtaining a pass. You can also take a detour.','ring','bicycle'),(1,'Citadel Park','Walking tour through Citadel Park in Poznan\nThere are two museums in the park - the \"Poznan\" Army Museum and the Armaments Museum. Near the Monument to the Heroes, there is a monument to the Marshal of the Soviet Union, Vasily Chuikov (1900-82), the commander of the 8th Guards Army, whose troops stormed the Citadel in 1945, unveiled in 1982.','cytadela','walking'),(2,'Gniezno - Poznan around Malta lake','From Gniezno along the S5 via the service station to Lubow and Kostrzyn, then along the old 2 and from Jasin by the pavement, in Antonin near VW via the underground passage to Leszka street, a kilometer through the forest and you are there! Add 50 km to return to Gniezno the same way','malta','bicycle'),(3,'Morasko Meteorite Reserve','The route starts from the forest parking lot towards the north to the meteorite craters. The beginning of the educational path. Natural Monument. Craters. Elm and autumn riparian forests. Former forester\'s lodge. Former gravel pit. Morasko Mountain. Peatbog. Cold Water. End of the educational path. END - Forest parking.','morasko','walking'),(4,'Poznan two castles','Walking tour to the Royal Castle and the Imperial Castle in Poznan.\nThe Royal Castle in Poznan is the oldest surviving royal residence in Poland. The building was erected on Przemysla Hill in the 13th century, first as a residential tower for Przemysl I, and later expanded by his son Przemysl II - the first king of Poland after the division into districts. The death of Przemysl II prevented the completion of the work, it was only in the 14th century that Casimir the Great did it, during which the castle became the largest secular building in Europe at that time.\nThe Imperial Castle is the last and youngest royal residence in Europe. It was built for the German Emperor Wilhelm II, designed by Franz Schwechten in 1905-1910. It was erected on the model of medieval castles - as a symbol of German rule in Greater Poland.','castle','walking'),(5,'Rusalka-Strzeszyn-Kiekrz','The route is mainly intended for amateurs of Sunday rides. Most of the route runs through forests and their edges. \nThe road is mostly compacted gravel or black earth, \nonly in the vicinity of Lake Kierskie we have asphalt, \nbut there is no car traffic (there are posts blocking the \nentrances). The greatest difficulty may be Sunday strollers \nwith children walking along the entire width of the paths \n(who are rarely led by the hand - sections to Rusalka and \nStrzeszyn) and the crossing of ul. Lutycka (Poznan bypass) - \nwe will cross it twice.','rusalka','bicycle');
/*!40000 ALTER TABLE `routes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `routes_times`
--

DROP TABLE IF EXISTS `routes_times`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `routes_times` (
  `date` datetime DEFAULT NULL,
  `score` time DEFAULT NULL,
  `route_id` int(11) NOT NULL,
  `user` varchar(30) DEFAULT NULL,
  KEY `route_id` (`route_id`),
  CONSTRAINT `routes_times_ibfk_1` FOREIGN KEY (`route_id`) REFERENCES `routes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `routes_times`
--

LOCK TABLES `routes_times` WRITE;
/*!40000 ALTER TABLE `routes_times` DISABLE KEYS */;
INSERT INTO `routes_times` VALUES ('2022-07-09 09:19:13','00:00:03',0,'Witold'),('2022-07-09 09:19:32','00:00:05',0,'Witold'),('2022-07-29 07:51:13','00:00:06',2,'Witold'),('2022-07-29 07:51:26','00:00:08',2,'Witold'),('2022-07-29 07:51:46','00:00:03',5,'Witold'),('2022-07-29 07:52:01','00:00:09',5,'Witold'),('2022-07-29 08:11:49','00:00:02',1,'Witold');
/*!40000 ALTER TABLE `routes_times` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `points`
--

DROP TABLE IF EXISTS `points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `points` (
  `route_id` int(11) NOT NULL,
  `point_no` int(11) NOT NULL,
  `point_name` varchar(30) DEFAULT NULL,
  `latitude` decimal(18,15) DEFAULT NULL,
  `longitude` decimal(18,15) DEFAULT NULL,
  PRIMARY KEY (`route_id`,`point_no`),
  CONSTRAINT `points_ibfk_1` FOREIGN KEY (`route_id`) REFERENCES `routes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `points`
--

LOCK TABLES `points` WRITE;
/*!40000 ALTER TABLE `points` DISABLE KEYS */;
INSERT INTO `points` VALUES (0,0,'Kornik',52.262486982945550,17.089942850321115),(0,1,'Rogalin',52.234415086319010,16.936700242056556),(0,2,'Steszew',52.283918489198925,16.700999380428815),(0,3,'Biedrusko',52.543913434188450,16.946769656885540),(0,4,'Kostrzyn',52.397968706794220,17.228036352509225),(0,5,'',52.262486982945550,17.089942850321115),(1,0,'Army Museum',52.417447243581385,16.933028991476170),(1,1,'Armaments Museum',52.420516065748860,16.933018262667020),(2,0,'Gniezno',52.615136505732230,17.586395188350227),(2,1,'Lubowo',52.510785463249740,17.446319499942902),(2,2,'Kostrzyn',52.397807365462526,17.222473056703755),(2,3,'Jasin',52.405348257310000,17.099048529358466),(2,4,'Antoninek',52.409704476147810,17.028137528725360),(2,5,'',52.400626032683670,17.017011725551306),(2,6,'Malta',52.404645127044624,16.976649844092435),(3,0,'Parking lot',52.487902947865340,16.897841836051242),(3,1,'Craters',52.490024857584670,16.896048733786568),(3,2,'',52.490121174065490,16.890477802446938),(3,3,'',52.486306516954450,16.890658754963800),(3,5,'Morasko mountain',52.484686280911010,16.893315151735270),(3,6,'Cold Water Lake',52.481811413450430,16.896083191533760),(4,0,'Royal Castle',52.409317788800784,16.931056427083560),(4,1,'Imperial Castle',52.408968529838106,16.919474873880790),(5,0,'Rusalka',52.429044358865630,16.876823563776455),(5,1,'Strzeszyn',52.463287434000820,16.825818585048470),(5,2,'Kiekrz',52.473965635937375,16.783705624781632);
/*!40000 ALTER TABLE `points` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `nick` varchar(30) NOT NULL,
  `password` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`nick`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('Tomasz','haslo'),('Witold','haslo');
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

-- Dump completed on 2022-07-29 23:32:04
