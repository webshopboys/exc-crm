RENAME TABLE S_SYSTEM TO P_SYSTEM;
RENAME TABLE T_WORKPLACE TO P_WORKPLACE;
RENAME TABLE T_WORKGROUP TO P_WORKGROUP;
RENAME TABLE T_FUNCTION TO P_FUNCTION;
RENAME TABLE T_JOBTITLE TO P_JOBTITLE;
RENAME TABLE T_ROLE TO P_ROLE;

alter table `T_STAFF` 
   add column `weekly_hours` int DEFAULT '40' NULL COMMENT 'Heti óraszáma, a cafetéria alapja' after `employ_time`;

CREATE TABLE `P_CAFETERIA_CAT`( 
   `id_cafeteria_cat` INT NOT NULL AUTO_INCREMENT COMMENT 'A cafeteria kategória azonosítója', 
   `category_name` VARCHAR(200) NOT NULL COMMENT 'A kategória neve', 
   `category_info` VARCHAR(3999) COMMENT 'Részletes magyarázat', 
   `max_limit` DECIMAL COMMENT 'Ha van havi maximuma', 
   `enabled` BOOLEAN COMMENT 'Ezévi módosításban kiosztható', 
   PRIMARY KEY (`id_cafeteria_cat`)
 );
 
 CREATE TABLE `T_CAFETERIA`( 
   `id_cafeteria` INT NOT NULL AUTO_INCREMENT COMMENT 'A havi cafeteria azonosítója.', 
   `id_staff` INT NOT NULL COMMENT 'A munkatárs.', 
   `id_cafeteria_cat` INT NOT NULL COMMENT 'A cafetéria kategória,', 
   `year_key` INT NOT NULL COMMENT 'Az év', 
   `month_key` INT NOT NULL COMMENT 'A hónap', 
   `amount` NUMERIC COMMENT 'Az összeg', 
   `updated` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'A módosítás ideje', 
   `updater` VARCHAR(100) COMMENT 'A módosító felhasználó',
   PRIMARY KEY (`id_cafeteria`)
 );
 
 ALTER TABLE `T_CAFETERIA` ADD CONSTRAINT `FK_CAF_CAT` FOREIGN KEY (`id_cafeteria_cat`) REFERENCES `P_CAFETERIA_CAT` (`id_cafeteria_cat`); 
 ALTER TABLE `T_CAFETERIA` ADD CONSTRAINT `FK_CAF_STAFF` FOREIGN KEY (`id_staff`) REFERENCES `T_STAFF` (`id_staff`); 
 ALTER TABLE `T_CAFETERIA` ADD INDEX `IX_CAF_YEAR` (`year_key`); 
 ALTER TABLE `T_CAFETERIA` ADD INDEX `IX_CAF_YEAR_MONTH` (`year_key`, `month_key`); 

 CREATE TABLE `T_CAFETERIA_INFO`( 
   `id_caf_info` INT NOT NULL AUTO_INCREMENT COMMENT 'A megjegyzés azonosítója.', 
   `year_key` INT NOT NULL COMMENT 'Az év', 
   `year_limit` NUMERIC COMMENT 'Az éves összeg',
   `info1` VARCHAR(3999) COMMENT 'Első megjegyzés',
   `info2` VARCHAR(3999) COMMENT 'Második megjegyzés' ,
   `updated` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'A módosítás ideje', 
   `updater` VARCHAR(100) COMMENT 'A módosító felhasználó',
   PRIMARY KEY (`id_caf_info`)
 );
 
 ALTER TABLE `T_CAFETERIA_INFO` ADD INDEX `IX_CAFINFO_YEAR` (`year_key`); 
 
 ALTER TABLE `P_CAFETERIA_CAT` 
   ADD COLUMN `yearly_limit` INT NULL COMMENT 'Ha van éves maximuma' AFTER `category_info`,
   CHANGE `max_limit` `monthly_limit` INT NULL  COMMENT 'Ha van havi maximuma'; 
 

 create table `P_CAFETERIA_LIMIT`( 
   `id_cafeteria_limit` int NOT NULL AUTO_INCREMENT , 
   `limit_label` varchar(50) NOT NULL COMMENT 'Az éves sávozás megnevezése', 
   `minimum_years` int NOT NULL COMMENT 'Az sáv éveinek száma ami felett érvényes', 
   `yearly_limit` int NOT NULL COMMENT 'A sávban érvényes éves összeg', 
   PRIMARY KEY (`id_cafeteria_limit`)
 );


INSERT INTO `P_CAFETERIA_LIMIT` (`limit_label`, `minimum_years`,`yearly_limit`) VALUES    ('1-3 évig', 1, 6000);
INSERT INTO `P_CAFETERIA_LIMIT` (`limit_label`, `minimum_years`,`yearly_limit`) VALUES    ('3-6 évig', 3, 7000);
INSERT INTO `P_CAFETERIA_LIMIT` (`limit_label`, `minimum_years`,`yearly_limit`) VALUES    ('6-10 évig', 6, 11000);
INSERT INTO `P_CAFETERIA_LIMIT` (`limit_label`, `minimum_years`,`yearly_limit`) VALUES    ('11 év felett', 10, 12000);



INSERT INTO`P_CAFETERIA_CAT`(`category_name`,`category_info`, `yearly_limit`,`monthly_limit`,`enabled`)  
VALUES ('Erzsébet étkezési utalvány', 'Az étkezési utalvány hideg és meleg étel vásárlására jogosító utalvány. A dolgozó maximum havi 8.000.- Ft értékben részesülhet étkeztetési juttatásban.  Felhasználható élelmiszer áruházakban (Tesco, Spar, Lidl. Penny, Auchan, CBA, Real- és Coop üzletekben), gyorséttermekben, ételszállító cégeknél, éttermekben és büfében.', 
8000, NULL, 1);

INSERT INTO`P_CAFETERIA_CAT`(`category_name`,`category_info`, `yearly_limit`,`monthly_limit`,`enabled`)  
VALUES ('Iskolakezdési utalvány', 'A juttatás az iskoláskorú, családi pótlékra jogosult tanuló gyermek tanévkezdési költségeihez nyújt segítséget a szülők részére. A beiskolázási utalvány kizárólag taneszköz, vagy ruházat vásárlására jogosít.', 
NULL, 30450, 1);

INSERT INTO`P_CAFETERIA_CAT`(`category_name`,`category_info`, `yearly_limit`,`monthly_limit`,`enabled`)  
VALUES ('Önkéntes nyugdíjpénztár', 'Az önkéntes nyugdíjpénztári befizetések célja az aktívkori életszínvonal megteremtése a nyugdíjas korban is. Amennyiben a munkavállaló megteheti, jelenlegi jövedelmét ezen a módon átcsoportosíthatja a jövőre. Minél hosszabb ideig (akár 30 évig) tart a megtakarítás, annál magasabb hozam érhető el.', 
NULL, NULL, 1);


INSERT INTO`P_CAFETERIA_CAT`(`category_name`,`category_info`, `yearly_limit`,`monthly_limit`,`enabled`)  
VALUES ('SZÉP kártya szálláshely alszámla', 'Felhasználása ugyanúgy működik, mint a  bankkártyás fizetés. A szép kártyákat cégünk központilag megrendelte, melyet ingyen megkapott minden igénylő. A választott juttatás összege erre kártyára kerül átutalásra. A szálláshely alszámla: meghatározott szálláshelyi szolgáltatásra használható fel (az elfogadóhelyek listája még nagyon kevés, de folyamatosan bővül).', 
NULL, 225000, 1);


INSERT INTO`P_CAFETERIA_CAT`(`category_name`,`category_info`, `yearly_limit`,`monthly_limit`,`enabled`)  
VALUES ('SZÉP kártya vendéglátás alszámla', 'Felhasználása ugyanúgy működik, mint a  bankkártyás fizetés. A szép kártyákat cégünk központilag megrendelte, melyet ingyen megkapott minden igénylő. A választott juttatás összege erre kártyára kerül átutalásra. A vendéglátás alszámla: éttermekben, melegkonyhás vendéglátóhelyeken, ételfutár cégeknél használható.', 
NULL, 150000, 1);


INSERT INTO`P_CAFETERIA_CAT`(`category_name`,`category_info`, `yearly_limit`,`monthly_limit`,`enabled`)  
VALUES ('SZÉP kártya szabadidő alszámla', 'Felhasználása ugyanúgy működik, mint a  bankkártyás fizetés. A szép kártyákat cégünk központilag megrendelte, melyet ingyen megkapott minden igénylő. A választott juttatás összege erre kártyára kerül átutalásra. A szabadidő alszámla: rekreáció, egészségmegőrzést szolgáló szolgáltatásokra használható.', 
NULL, 75000, 1);


INSERT INTO`P_CAFETERIA_CAT`(`category_name`,`category_info`, `yearly_limit`,`monthly_limit`,`enabled`)  
VALUES ('Helyi utazási bérlet', 'A munkáltató a dolgozó választása esetén a keretösszeg terhére, a munkába járáshoz havi helyi utazási bérletet bocsát a dolgozó rendelkezésére.', 
NULL, NULL, 1);


INSERT INTO`P_CAFETERIA_CAT`(`category_name`,`category_info`, `yearly_limit`,`monthly_limit`,`enabled`)  
VALUES ('Sport utalvány', 'Az Erzsébet sport utalvány felhasználásával a munkavállaló a sportrendezvényekre szóló belépőjegyet vásárolhatja meg, pl. focimeccs, kézi és kosárlabda meccs, de bármilyen sporteseményre szóló belépőjegy vagy bérlet vásárolható. Nem használható fittness és edzőtermi belépőre.', 
NULL, NULL, 1);

INSERT INTO`P_CAFETERIA_CAT`(`category_name`,`category_info`, `yearly_limit`,`monthly_limit`,`enabled`)  
VALUES ('Kultúra utalvány', 'Az Erzsébet kultúra utalvány felhasználható kulturális szolgáltatások igénybe vételére, ezek: színház, múzeum, kiállítások, tánc, cirkusz vagy zeneművészeti előadások látogatására. Mozijegy vásárlására nem használható.', 
NULL, 50000, 1);
