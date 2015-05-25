/*
SQLyog Community v9.20 
MySQL - 5.6.14 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;


update  `T_WORKGROUP` SET `address`='1051 Bp., Szent István tér 3.', `tax_number`='111-111-11111111111', `company_number`='0000000000000', `representative`='Kardos Ildikó', `company_name`='Exclusive Best Change Kft.' WHERE id_workgroup = 18;

update  `T_WORKGROUP` SET `address`='1051 Bp., Szent István tér 3.', `tax_number`='111-111-11111111111', `company_number`='0000000000000', `representative`='Kardos Ildikó', `company_name`='Exclusive East Change Kft.' WHERE id_workgroup = 19;

update  `T_WORKGROUP` SET `address`='1051 Bp., Szent István tér 3.', `tax_number`='111-111-11111111111', `company_number`='XXXXXXXX', `representative`='Kardos Ildikó', `company_name`='Exclusive Pannon Change Kft.' WHERE id_workgroup = 20;

update  `T_WORKGROUP` SET `address`='1051 Bp., Szent István tér 3.', `tax_number`='111-111-11111111111', `company_number`='XXXXXXXX', `representative`='Kardos Ildikó', `company_name`='Exclusive Best Change Kft.' WHERE id_workgroup = 21;

update  `T_WORKGROUP` SET `address`='1051 Bp., Szent István tér 3.', `tax_number`='111-111-11111111111', `company_number`='XXXXXXXX', `representative`='Kardos Ildikó', `company_name`='Exclusive Best Change Kft.' WHERE id_workgroup = 22;

update  `T_WORKGROUP` SET `address`='1051 Bp., Szent István tér 3.', `tax_number`='111-111-11111111111', `company_number`='XXXXXXXX', `representative`='Kardos Ildikó', `company_name`='Exclusive East Change Kft.' WHERE id_workgroup = 23;

update  `T_WORKGROUP` SET `address`='1051 Bp., Szent István tér 3.', `tax_number`='111-111-11111111111', `company_number`='XXXXXXXX', `representative`='Kardos Ildikó', `company_name`='Exclusive East Change Kft.' WHERE id_workgroup = 24;

update  `T_WORKGROUP` SET `address`='1051 Bp., Szent István tér 3.', `tax_number`='111-111-11111111111', `company_number`='XXXXXXXX', `representative`='Kardos Ildikó', `company_name`='Exclusive East Change Kft.' WHERE id_workgroup = 25;

update  `T_WORKGROUP` SET `address`='1051 Bp., Szent István tér 3.', `tax_number`='111-111-11111111111', `company_number`='XXXXXXXX', `representative`='Kardos Ildikó', `company_name`='Exclusive East Change Kft.' WHERE id_workgroup = 26;

update  `T_WORKGROUP` SET `address`='1051 Bp., Szent István tér 3.', `tax_number`='111-111-11111111111', `company_number`='XXXXXXXX', `representative`='Kardos Ildikó', `company_name`='Exclusive Pannon Change Kft.' WHERE id_workgroup = 27;

update  `T_WORKGROUP` SET `address`='1051 Bp., Szent István tér 3.', `tax_number`='111-111-11111111111', `company_number`='XXXXXXXX', `representative`='Kardos Ildikó', `company_name`='Exclusive Pannon Change Kft.' WHERE id_workgroup = 28;

update  `T_WORKGROUP` SET `address`='1051 Bp., Szent István tér 3.', `tax_number`='111-111-11111111111', `company_number`='XXXXXXXX', `representative`='Kardos Ildikó', `company_name`='Exclusive Pannon Change Kft.' WHERE id_workgroup = 29;



create table `T_WORKGROUP` (
	`id_workgroup` int (11),
	`group_name` varchar (300),
	`address` varchar (600),
	`tax_number` varchar (60),
	`company_number` varchar (300),
	`representative` varchar (300),
	`company_name` varchar (300)
); 
insert into `T_WORKGROUP` (`id_workgroup`, `group_name`, `address`, `tax_number`, `company_number`, `representative`, `company_name`) values('18','Exclusive Best Change','1051 Bp., Szent István tér 3','0000000000000','111-111-11111111111','Kardos Ildikó','Exclusive Best Change Kft.');
insert into `T_WORKGROUP` (`id_workgroup`, `group_name`, `address`, `tax_number`, `company_number`, `representative`, `company_name`) values('19','Exclusive East Change','1051 Bp., Szent István tér 3.','000000','111-111-11111111111','Kardos Ildikó','Exclusive East Change Kft.');
insert into `T_WORKGROUP` (`id_workgroup`, `group_name`, `address`, `tax_number`, `company_number`, `representative`, `company_name`) values('20','Exclusive Pannon Change','1051 Bp., Szent István tér 3.','0000000000000','111-111-11111111111','Kardos Ildikó','Exclusive Pannon Change Kft.');
insert into `T_WORKGROUP` (`id_workgroup`, `group_name`, `address`, `tax_number`, `company_number`, `representative`, `company_name`) values('21','Best Szeged','1051 Bp., Szent István tér 3.','0000000000000','111-111-11111111111','Kardos Ildikó','Exclusive Best Change Kft.');
insert into `T_WORKGROUP` (`id_workgroup`, `group_name`, `address`, `tax_number`, `company_number`, `representative`, `company_name`) values('22','Best Kecskemét','1051 Bp., Szent István tér 3.','0000000000000','111-111-11111111111','Kardos Ildikó','Exclusive Best Change Kft.');
insert into `T_WORKGROUP` (`id_workgroup`, `group_name`, `address`, `tax_number`, `company_number`, `representative`, `company_name`) values('23','East Békéscsaba','1051 Bp., Szent István tér 3.','0000000000000','111-111-11111111111','Kardos Ildikó','Exclusive East Change Kft.');
insert into `T_WORKGROUP` (`id_workgroup`, `group_name`, `address`, `tax_number`, `company_number`, `representative`, `company_name`) values('24','East Debrecen','1051 Bp., Szent István tér 3.','0000000000000','111-111-11111111111','Kardos Ildikó','Exclusive East Change Kft.');
insert into `T_WORKGROUP` (`id_workgroup`, `group_name`, `address`, `tax_number`, `company_number`, `representative`, `company_name`) values('25','East Nyíregyháza','1051 Bp., Szent István tér 3.','0000000000000','111-111-11111111111','Kardos Ildikó','Exclusive East Change Kft.');
insert into `T_WORKGROUP` (`id_workgroup`, `group_name`, `address`, `tax_number`, `company_number`, `representative`, `company_name`) values('26','East Iroda','1051 Bp., Szent István tér 3.','0000000000000','111-111-11111111111','Kardos Ildikó','Exclusive East Change Kft.');
insert into `T_WORKGROUP` (`id_workgroup`, `group_name`, `address`, `tax_number`, `company_number`, `representative`, `company_name`) values('27','Pannon Pécs','1051 Bp., Szent István tér 3.','0000000000000','111-111-11111111111','Kardos Ildikó','Exclusive Pannon Change Kft.');
insert into `T_WORKGROUP` (`id_workgroup`, `group_name`, `address`, `tax_number`, `company_number`, `representative`, `company_name`) values('28','Pannon Kaposvár','1051 Bp., Szent István tér 3.','0000000000000','111-111-11111111111','Kardos Ildikó','Exclusive Pannon Change Kft.');
insert into `T_WORKGROUP` (`id_workgroup`, `group_name`, `address`, `tax_number`, `company_number`, `representative`, `company_name`) values('29','Pannon Szekszárd','1051 Bp., Szent István tér 3.','0000000000000','111-111-11111111111','Kardos Ildikó','Exclusive Pannon Change Kft.');
