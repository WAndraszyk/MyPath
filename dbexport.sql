CREATE DATABASE  IF NOT EXISTS `fragmenty` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `fragmenty`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: fragmenty
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
  `name` varchar(50) NOT NULL DEFAULT '',
  `way` varchar(1000) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `routes`
--

LOCK TABLES `routes` WRITE;
/*!40000 ALTER TABLE `routes` DISABLE KEYS */;
INSERT INTO `routes` VALUES ('Bicycle Ring of the Poznan Powiat','The bicycle route around Poznan with seven connecting routes leading to it from the city center was conceived as a network of tourist routes. The route passes through interesting regions (Wielkopolski National Park, Puszcza Zielonka Landscape Park, Promno Landscape Park, Rogalinski Landscape Park and the most famous towns in the vicinity of the capital of Wielkopolska). The trail goes through Kornik, Rogalin, Steszew, Biedrusko, and Kostrzyn. The route runs through the military training ground in Biedrusko, access to which is possible after obtaining a pass. You can also take a detour.','ring','bicycle'),('Citadel Park','Walking tour through Citadel Park in Poznan\nThere are two museums in the park - the \"Poznan\" Army Museum and the Armaments Museum. Near the Monument to the Heroes, there is a monument to the Marshal of the Soviet Union, Vasily Chuikov (1900-82), the commander of the 8th Guards Army, whose troops stormed the Citadel in 1945, unveiled in 1982.','cytadela','walking'),('Gniezno - Poznan around Malta lake','From Gniezno along the S5 via the service station to Lubow and Kostrzyn, then along the old 2 and from Jasin by the pavement, in Antonin near VW via the underground passage to Leszka street, a kilometer through the forest and you are there! Add 50 km to return to Gniezno the same way','malta','bicycle'),('Morasko Meteorite Reserve','The route starts from the forest parking lot towards the north to the meteorite craters. The beginning of the educational path. Natural Monument. Craters. Elm and autumn riparian forests. Former forester\'s lodge. Former gravel pit. Morasko Mountain. Peatbog. Cold Water. End of the educational path. END - Forest parking.','morasko','walking'),('Poznan two castles','Walking tour to the Royal Castle and the Imperial Castle in Poznan.\nThe Royal Castle in Poznan is the oldest surviving royal residence in Poland. The building was erected on Przemysla Hill in the 13th century, first as a residential tower for Przemysl I, and later expanded by his son Przemysl II - the first king of Poland after the division into districts. The death of Przemysl II prevented the completion of the work, it was only in the 14th century that Casimir the Great did it, during which the castle became the largest secular building in Europe at that time.\nThe Imperial Castle is the last and youngest royal residence in Europe. It was built for the German Emperor Wilhelm II, designed by Franz Schwechten in 1905-1910. It was erected on the model of medieval castles - as a symbol of German rule in Greater Poland.','castle','walking'),('Rusalka-Strzeszyn-Kiekrz','The route is mainly intended for amateurs of Sunday rides. Most of the route runs through forests and their edges. \nThe road is mostly compacted gravel or black earth, \nonly in the vicinity of Lake Kierskie we have asphalt, \nbut there is no car traffic (there are posts blocking the \nentrances). The greatest difficulty may be Sunday strollers \nwith children walking along the entire width of the paths \n(who are rarely led by the hand - sections to Rusalka and \nStrzeszyn) and the crossing of ul. Lutycka (Poznan bypass) - \nwe will cross it twice.','rusalka','bicycle');
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
  `route` varchar(50) DEFAULT NULL,
  `user` varchar(30) DEFAULT NULL,
  KEY `route` (`route`),
  CONSTRAINT `routes_times_ibfk_1` FOREIGN KEY (`route`) REFERENCES `routes` (`name`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `routes_times`
--

LOCK TABLES `routes_times` WRITE;
/*!40000 ALTER TABLE `routes_times` DISABLE KEYS */;
INSERT INTO `routes_times` VALUES ('2022-07-09 09:19:13','00:00:03','Bicycle Ring of the Poznan Powiat','Witold'),('2022-07-09 09:19:32','00:00:05','Bicycle Ring of the Poznan Powiat','Witold');
/*!40000 ALTER TABLE `routes_times` ENABLE KEYS */;
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
  `is_logged_in` int(11) DEFAULT NULL,
  PRIMARY KEY (`nick`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('Tomasz','haslo',0),('Witold','haslo',1);
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

-- Dump completed on 2022-07-16 13:29:26
