
/*
create table `p_system` (
	`sysgroup` varchar (135),
	`syskey` varchar (135),
	`sysparam` varchar (135),
	`sysorder` int (11)
); */

TRUNCATE TABLE P_SYSTEM;
 
insert into `P_SYSTEM` (`sysgroup`, `syskey`, `sysparam`, `sysorder`) values('EBC','RELEASE','2015.09.08','0');
insert into `P_SYSTEM` (`sysgroup`, `syskey`, `sysparam`, `sysorder`) values('EBC','SYSTEM','EXC','0');
insert into `P_SYSTEM` (`sysgroup`, `syskey`, `sysparam`, `sysorder`) values('EBC','VERSION','0.5','0');
insert into `P_SYSTEM` (`sysgroup`, `syskey`, `sysparam`, `sysorder`) values('SYS','VERSION','0.5','0');
