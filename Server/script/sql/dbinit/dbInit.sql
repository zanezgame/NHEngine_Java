USE `wind_manage`;
replace into base_info (server_type,white_ip_open) values (1,0);

USE `wind_gm`;
replace into gm_user (id,name,pass) values (1,'admin','08082013');
replace into server (type,name,manage_ip,manage_port) values (1,'苹果官方','192.168.1.100',8000);