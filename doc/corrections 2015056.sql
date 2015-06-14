-- adminBqBrK4n  M5y6Q11mrrK9

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

-- RUN 20150520

alter table `T_CRMUSER` 
   add column `user_system` varchar(50) DEFAULT 'EBC' NOT NULL COMMENT 'A használt rendszer. EBC vagy ART.' ;

-- RUN 20150612   


