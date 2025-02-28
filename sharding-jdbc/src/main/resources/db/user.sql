CREATE DATABASE
IF NOT EXISTS sharding-jdbc DEFAULT CHARACTER
SET utf8 DEFAULT COLLATE utf8_general_ci;
USE sharding-jdbc;
/*Table structure for table `user_0` */
DROP TABLE IF EXISTS `user_0`;
CREATE TABLE `user_0` (
                          `id` int(12) NOT NULL AUTO_INCREMENT,
                          `username` varchar(12) NOT NULL,
                          `password` varchar(30) NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `idx-username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8;
/*Table structure for table `user_1` */
DROP TABLE IF EXISTS `user_1`;
CREATE TABLE `user_1` (
                          `id` int(12) NOT NULL AUTO_INCREMENT,
                          `username` varchar(12) NOT NULL,
                          `password` varchar(30) NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `idx-username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8;
/*Table structure for table `user_2` */
DROP TABLE IF EXISTS `user_2`;
CREATE TABLE `user_2` (
                          `id` int(12) NOT NULL AUTO_INCREMENT,
                          `username` varchar(12) NOT NULL,
                          `password` varchar(30) NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `idx-username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8;
/*Table structure for table `user_3` */
DROP TABLE IF EXISTS `user_3`;
CREATE TABLE `user_3` (
                          `id` int(12) NOT NULL AUTO_INCREMENT,
                          `username` varchar(12) NOT NULL,
                          `password` varchar(30) NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `idx-username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8;
/*Table structure for table `user_4` */
DROP TABLE IF EXISTS `user_4`;
CREATE TABLE `user_4` (
                          `id` INT(12) NOT NULL AUTO_INCREMENT,
                          `username` VARCHAR(12) NOT NULL,
                          `password` VARCHAR(30) NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `idx-username` (`username`)
) ENGINE=INNODB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8;