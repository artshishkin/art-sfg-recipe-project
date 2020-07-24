## Use to run mysql db docker image, optional if you're not using a local mysqldb
# docker run --name mysqldb -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d mysql
# docker run --name art-mysql -v /home/art/dockerdata/mysql:/var/lib/mysql -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d mysql

# connect to mysql and run as root user
#Create Databases
CREATE DATABASE art_dev;
CREATE DATABASE art_prod;

#Create database service accounts
CREATE USER 'art_dev_user'@'localhost' IDENTIFIED BY 'pass123';
CREATE USER 'art_prod_user'@'localhost' IDENTIFIED BY 'pass123';
CREATE USER 'art_dev_user'@'%' IDENTIFIED BY 'pass123';
CREATE USER 'art_prod_user'@'%' IDENTIFIED BY 'pass123';

#Database grants
GRANT SELECT ON art_dev.* to 'art_dev_user'@'localhost';
GRANT INSERT ON art_dev.* to 'art_dev_user'@'localhost';
GRANT DELETE ON art_dev.* to 'art_dev_user'@'localhost';
GRANT UPDATE ON art_dev.* to 'art_dev_user'@'localhost';
GRANT SELECT ON art_prod.* to 'art_prod_user'@'localhost';
GRANT INSERT ON art_prod.* to 'art_prod_user'@'localhost';
GRANT DELETE ON art_prod.* to 'art_prod_user'@'localhost';
GRANT UPDATE ON art_prod.* to 'art_prod_user'@'localhost';
GRANT SELECT ON art_dev.* to 'art_dev_user'@'%';
GRANT INSERT ON art_dev.* to 'art_dev_user'@'%';
GRANT DELETE ON art_dev.* to 'art_dev_user'@'%';
GRANT UPDATE ON art_dev.* to 'art_dev_user'@'%';
GRANT SELECT ON art_prod.* to 'art_prod_user'@'%';
GRANT INSERT ON art_prod.* to 'art_prod_user'@'%';
GRANT DELETE ON art_prod.* to 'art_prod_user'@'%';
GRANT UPDATE ON art_prod.* to 'art_prod_user'@'%';
