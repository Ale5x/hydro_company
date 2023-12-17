CREATE SCHEMA hydro_company;

create table roles (
  id_roles bigint primary key auto_increment,
  name varchar(15)
);

create table companies (
  id_companies bigint primary key auto_increment,
  name varchar(45),
  address varchar(100)
);

create table users (
  id_users bigint primary key auto_increment,
  first_name varchar(45),
  last_name varchar(45),
  password varchar(320),
  path varchar(255),
  id_companies bigint,
  registration timestamp,
  id_roles bigint,
foreign key (id_companies) references companies (id_companies),
foreign key (id_roles) references roles (id_roles)
);

