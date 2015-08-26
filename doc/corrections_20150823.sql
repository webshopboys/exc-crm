ALTER TABLE `exc`.`T_CAFETERIA_INFO` ADD COLUMN `id_staff` INT(11) NOT NULL COMMENT 'Az �rintett munkat�rs' AFTER `id_caf_info`;

ALTER TABLE `exc`.`T_CAFETERIA_INFO` MODIFY `updater` VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'A m�dos�t� felhaszn�l�'; 

ALTER TABLE `exc`.`T_CAFETERIA_INFO` ADD CONSTRAINT `FK_CAF_INFO_STAFF` FOREIGN KEY (`id_staff`) REFERENCES `T_STAFF` (`id_staff`); 