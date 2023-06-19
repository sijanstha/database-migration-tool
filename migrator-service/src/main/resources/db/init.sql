create database database_migration_tool;

create table tenant_info(
    id int auto_increment primary key not null,
    connection_id varchar(255) unique not null,
    mysql_connection_url varchar(255) not null,
    mysql_user_name varchar(100) not null,
    mysql_user_password varchar(255),
    mysql_db_name varchar(200) not null,
    created_at datetime not null
);