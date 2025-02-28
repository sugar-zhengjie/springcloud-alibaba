配置MySQL主从复制

#1、分别在主节点128和131上面my.cnf配置bin log信息，并且重启服务
server-id = 配置不重复的server-id
log-bin = master-bin
log-bin-index = master-bin.index
#2、分别在从节点129，130，132，133上面my.cnf配置relay log信息，并且重启
server-id = 配置不重复的server-id
relay-log-index = relay-log.index
relay-log = relay-log
slave-skip-errors = all
#3、分别在128和131上创建主从连接账号
#删除原有主从连接账号，你高兴可以不删
delete from mysql.user where user = 'repl';
#创建主从连接账户repl
CREATE USER repl;
#或者执行操作如下创建：从库repl密码123456 跟主库连接，有slave的权限      
GRANT replication slave ON *.* TO 'repl'@'192.168.223.%' identified by '123456';
#刷新生效
FLUSH PRIVILEGES;
#4、分别在129，130上建立与128的主从关系，132，133上建立与131的主从关系：
mysql> stop slave;
Query OK, 0 rows affected, 1 warning (0.00 sec)
mysql> reset slave;
Query OK, 0 rows affected (0.28 sec)
mysql> CHANGE MASTER TO
-> master_host='192.168.223.128',#128或者131
-> master_port=3306,
-> master_user='repl',
-> master_password='123456',
-> master_log_file='master-bin.000017',#你知道怎么查
-> master_log_pos=120;#你知道怎么查
Query OK, 0 rows affected, 2 warnings (0.04 sec)
mysql> start slave;
Query OK, 0 rows affected (0.00 sec)
mysql> show slave status;#查看是否成功
#PS:注意各节点server_id和server_uuid要不重复