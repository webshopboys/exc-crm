set global max_allowed_packet=104857600;

ALTER TABLE `T_STAFF` ADD COLUMN `accountNumber` VARCHAR(30) NULL COMMENT 'Számlaszáma' AFTER `children`; 

alter table `T_CONTRACTDOC` 
   change `document_type` `document_type` varchar(100) character set utf8 collate utf8_general_ci NOT NULL comment 'Tárolt dokumentum vagy sablon fajtája.'

ALTER TABLE `T_WORKGROUP` 
ADD COLUMN `address` VARCHAR(200) NULL AFTER `group_name`,
ADD COLUMN `tax_number` VARCHAR(20) NULL AFTER `address`,
ADD COLUMN `company_number` VARCHAR(100) NULL AFTER `tax_number`,
ADD COLUMN `representative` VARCHAR(100) NULL AFTER `company_number`,
ADD COLUMN `company_name` VARCHAR(100) NULL AFTER `representative`;










ALTER TABLE `K_STAFF_ALARM` 
CHANGE COLUMN `created` `closed` TIMESTAMP NULL ;

UPDATE `K_STAFF_ALARM` SET closed = null WHERE id_staffalarm > 0;

ALTER TABLE `K_STAFF_ALARM` 
CHANGE COLUMN `id_staff` `id_staff` INT(11) NOT NULL COMMENT 'A munkatárs akihez a risztókód / munkahely tartozik.' ,
CHANGE COLUMN `id_alarm` `id_alarm` INT(11) NOT NULL COMMENT 'A risztókód / munkahely azonosítója.' ,
CHANGE COLUMN `closed` `closed` TIMESTAMP NULL COMMENT 'Amennyiben megváltozik a hozzárendelés, úgy a korábbi ekkor ér véget. Egy munkatárshoz egyetlen opcionális null lezárású risztókód tartozhat.' ,
ADD UNIQUE INDEX `UI_SA_STAFF_ALARM_CLOSED` (`id_staff` ASC, `id_alarm` ASC, `closed` ASC);



ALTER TABLE `K_STAFF_JOBTITLE` 
CHANGE COLUMN `created` `closed` TIMESTAMP NULL ;

UPDATE `K_STAFF_JOBTITLE` SET closed = null WHERE id_staffjob > 0;

CREATE TABLE `H_STAFF_WORKGROUP` (
  `id_staff` int(11) NOT NULL COMMENT 'A munkatárs azonosítója.',
  `id_workgroup` int(11) NOT NULL COMMENT 'A munkatárs munkacsoportja.',
  `started` TIMESTAMP NOT NULL COMMENT 'Az érvényesség kezdete.',
  `ended` TIMESTAMP NOT NULL COMMENT 'Az érvényesség vége.',
  CONSTRAINT `FK_HSTAFFWG_STAFF` FOREIGN KEY (`id_staff`) REFERENCES `T_STAFF` (`id_staff`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_HSTAFFWG_WG` FOREIGN KEY (`id_workgroup`) REFERENCES `T_WORKGROUP` (`id_workgroup`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='A munkatársak munkacsoport változásai';


CREATE TABLE `H_STAFF_WORKPLACE` (
  `id_staff` int(11) NOT NULL COMMENT 'A munkatárs azonosítója.',
  `id_workplace` int(11) NOT NULL COMMENT 'A munkatárs munkahelye.',
  `started` TIMESTAMP NOT NULL COMMENT 'Az érvényesség kezdete.',
  `ended` TIMESTAMP NOT NULL COMMENT 'Az érvényesség vége.',
  CONSTRAINT `FK_HSTAFFWP_STAFF` FOREIGN KEY (`id_staff`) REFERENCES `T_STAFF` (`id_staff`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_HSTAFFWP_WP` FOREIGN KEY (`id_workplace`) REFERENCES `T_WORKPLACE` (`id_workplace`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='A munkatársak munkahely változásai';






CREATE TABLE `t_contractdoc` (
  `id_contractdoc` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Egyedi doc kulcs.',
  `document_type` varchar(45) NOT NULL COMMENT 'Tárolt dokumentum vagy sablon fajtája.',
  `document_url` varchar(300) DEFAULT NULL COMMENT 'Fájban tárolva annak útvonala.',
  `document_bin` longblob COMMENT 'Serializálva a dokumentum doc, pdf, stb. bájttömbje.',
  `id_staff` int(11) DEFAULT NULL COMMENT 'Amennyiben ez egy munkatárshoz tartozik, úgy annak azonosítója.',
  `document_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'A létrehozás dátuma.',
  `document_expire` timestamp NULL DEFAULT NULL COMMENT 'A lejárat dátuma.',
  `document_note` varchar(3999) DEFAULT NULL,
  PRIMARY KEY (`id_contractdoc`),
  KEY `IX_CONTRACTDOCTYPE` (`document_type`),
  KEY `IX_CONTRACTDOCSTAFF` (`id_staff`),
  KEY `IX_CONTRACTDOCCREATED` (`document_created`),
  CONSTRAINT `FK_CONTRACTDOC_STAFF` FOREIGN KEY (`id_staff`) REFERENCES `T_STAFF` (`id_staff`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='A munkatársak szerződéseinek tára';
