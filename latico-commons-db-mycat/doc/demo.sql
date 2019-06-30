-- 创建第一个数据库
CREATE DATABASE`test_mycat1`  DEFAULT CHARACTER SET utf8 ;
USE `test_mycat1`;
CREATE TABLE `demo` (
  `id` int(11) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(128) NOT NULL,
  `administrator` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 第二个库
CREATE DATABASE`test_mycat2`  DEFAULT CHARACTER SET utf8 ;
USE `test_mycat2`;
CREATE TABLE `demo`
(
  `id`            int(11) NOT NULL,
  `username`      varchar(32)  NOT NULL,
  `password`      varchar(128) NOT NULL,
  `administrator` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 第三个库
CREATE DATABASE`test_mycat3`  DEFAULT CHARACTER SET utf8 ;
USE `test_mycat3`;
CREATE TABLE `demo`
(
  `id`            int(11) NOT NULL,
  `username`      varchar(32)  NOT NULL,
  `password`      varchar(128) NOT NULL,
  `administrator` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


