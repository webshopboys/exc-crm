ALTER TABLE `exc`.`P_CAFETERIA_CAT` 
   ADD COLUMN `category_keys` VARCHAR(300) NULL COMMENT 'A ketegória kereső kifejezései, név alternativák' AFTER `enabled`;
   
   
UPDATE `exc`.P_CAFETERIA_CAT SET category_keys = 'Erzsébet étkezési utalvány, Erzsébet utalvány' WHERE id_cafeteria_cat = 1;
UPDATE `exc`.P_CAFETERIA_CAT SET category_keys = 'Iskolakezdési utalvány (Iskolakezdési .)' WHERE id_cafeteria_cat = 2;
UPDATE `exc`.P_CAFETERIA_CAT SET category_keys = 'Önkéntes nyugdíjpénztár (Önkéntes nyugdíj pénztár)' WHERE id_cafeteria_cat = 3;
UPDATE `exc`.P_CAFETERIA_CAT SET category_keys = 'SZÉP kártya szálláshely alszámla (SZÉP szállás) (SZÉP szálláshely) (SZÉP-szálláshely)' WHERE id_cafeteria_cat = 4;
UPDATE `exc`.P_CAFETERIA_CAT SET category_keys = 'SZÉP kártya vendéglátás alszámla (SZÉP vendéglátás) (SZÉP-vendéglátás)' WHERE id_cafeteria_cat = 5;
UPDATE `exc`.P_CAFETERIA_CAT SET category_keys = 'SZÉP kártya szabadidő alszámla (SZÉP szabadidő) (SZÉP-szabadidő)' WHERE id_cafeteria_cat = 6;
UPDATE `exc`.P_CAFETERIA_CAT SET category_keys = 'Helyi utazási bérlet (Bérlet)' WHERE id_cafeteria_cat = 7;
UPDATE `exc`.P_CAFETERIA_CAT SET category_keys = 'Sport utalvány' WHERE id_cafeteria_cat = 8;
UPDATE `exc`.P_CAFETERIA_CAT SET category_keys = 'Kultúra utalvány' WHERE id_cafeteria_cat = 9;



UPDATE `exc`.`P_SYSTEM` SET  `sysparam` = '0.5' WHERE `sysid` = 1 ;
UPDATE `exc`.`P_SYSTEM` SET  `sysparam` = '0.5' WHERE `sysid` = 2 ;
UPDATE `exc`.`P_SYSTEM` SET  `sysparam` = '2015.09.08' WHERE `sysid` = 4 ;


