/*
SQLyog Community v9.20 
MySQL - 5.6.14 : Database - exc
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`exc` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `exc`;

/*Table structure for table `t_function` */

DROP TABLE IF EXISTS `t_function`;

CREATE TABLE `t_function` (
  `id_function` int(11) NOT NULL AUTO_INCREMENT COMMENT 'A funkciók kulcsa.',
  `function_code` varchar(100) NOT NULL COMMENT 'A funkció INNO kódja. Ezzel van a menüben bekötve.',
  `function_name` varchar(100) NOT NULL COMMENT 'A funkció neve.',
  `function_url` varchar(200) DEFAULT NULL COMMENT 'A dinamikus feldolgozó képernyő útvonala.',
  PRIMARY KEY (`id_function`),
  UNIQUE KEY `function_code_UNIQUE` (`function_code`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COMMENT='A funkciók, egyben a jogok is.';

/*Data for the table `t_function` */

insert  into `T_FUNCTION`(`id_function`,`function_code`,`function_name`,`function_url`) values (1,'02-01-02','Munkatárs felvitele',NULL),(2,'02-01-01','Munkatársak listázása',NULL),(3,'02-01-01-01','Munkatárs alapadatok szerkesztése',NULL),(4,'02-01-01-04','Szerződés kezelése',NULL),(5,'EBC-M','Ellenőrzések menü',NULL),(15,'02-01-03','Munkatárs lista exportálása',NULL),(16,'02-01-01-07','Munkatárs állapotának módosítása',NULL),(17,'08-01-04','Jogok karbantartása',NULL),(18,'07-10-M','Beosztás menü',NULL),(19,'06-M','Naptár menü',NULL),(20,'03-M','Raktár menü',NULL),(21,'08-05-M','Cafetéria menü',NULL),(22,'02-02-M','Munkahelyek menü',NULL),(23,'09-M','Gépjármű menü',NULL),(24,'02-02-02','Munkahely megtekintése',NULL),(25,'02-01-M','Munkaügy menü',NULL),(26,'08-05-04-K','Csoport karbantartása',NULL),(27,'08-05-02','Munkakörök karbantartása',NULL),(28,'08-01','Rendszer beállítások megtekintése',NULL),(29,'08-01-M','Rendszer menü',NULL),(30,'08-05-01','Munkatárs jogának beállítása',NULL),(31,'08-05-04','Csoportok megtekintése',NULL),(34,'SZALL-M','Szállítói menü',NULL),(35,'HIANY-M','Hiányok menü',NULL),(36,'HAZIP-M','Házipénztár menü',NULL),(37,'02-02-02-K','Munkahely karbantartása',NULL),(38,'DEV-DEL','Fejlesztői törlési jog',NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
