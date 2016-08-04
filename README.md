# ActiveMQ-JDBCAuthenticationPlugin
ActiveMQ使用JDBC进行连接安全验证的插件。

DB_INIT

CREATE TABLE `conn_users` (
  `id` int(10) unsigned NOT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `groups` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
