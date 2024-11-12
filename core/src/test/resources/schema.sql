CREATE SCHEMA hydro_company;

create table roles (
  id_roles bigint primary key auto_increment,
  name varchar(15)
);

create table products_connection (
  id_products_connection bigint primary key auto_increment,
  size varchar(45)
);

create table countries (
  id_countries bigint primary key auto_increment,
  name varchar(45)
);

create table user_company (
  id_user_company bigint primary key auto_increment,
  name varchar(45),
  address varchar(100),
  countries_id bigint,
  foreign key (countries_id) references countries (id_countries)
);

create table companies_has_countries (
    id_companies bigint,
    id_countries bigint,
    PRIMARY KEY (id_companies, id_countries),
    FOREIGN KEY (id_companies) REFERENCES user_company(id_user_company),
    FOREIGN KEY (id_countries) REFERENCES countries(id_countries)
);

create table users (
  id_users bigint primary key auto_increment,
  first_name varchar(45),
  last_name varchar(45),
  email varchar(200),
  password varchar(320),
  path varchar(255),
  id_user_company bigint,
  registration timestamp,
  id_roles bigint,
foreign key (id_user_company) references user_company (id_user_company),
foreign key (id_roles) references roles (id_roles)
);

create table products_type (
  id_products_type bigint primary key auto_increment,
  name varchar(15)
);

create table product_companies (
  id_product_companies bigint primary key auto_increment,
  name varchar(30)
);

create table products (
  id_products bigint primary key auto_increment,
  id_products_type bigint,
  id_products_connection bigint,
  id_product_companies bigint,
  id_countries bigint,
  flow_rate bigint,
  count bigint,
  pressure bigint,
  pressure_max bigint,
  sku varchar(45),
  weight varchar(45),
  path_hydraulic_scheme varchar(200),
  additional_inf varchar(320),
  foreign key (id_countries) references countries (id_countries),
  foreign key (id_products_type) references products_type (id_products_type),
  foreign key (id_product_companies) references product_companies (id_product_companies),
  foreign key (id_products_connection) references products_connection (id_products_connection)
  on delete cascade on update cascade
);

create table pictures (
  id_pictures bigint primary key auto_increment,
  id_products bigint,
  path varchar(15),
  foreign key (id_products) references products (id_products)
  on delete cascade on update cascade
);


create table shelf (
  id_shelf bigint primary key auto_increment,
  name varchar(45)
);

create table storage_racks (
  id_racks bigint primary key auto_increment,
  name varchar(45),
  shelf_id bigint,
  foreign key (shelf_id) references shelf (id_shelf)
);

create table storage_racks_products (
  id_products bigint,
  id_storage_racks bigint
);

create table countries_has_product_companies (
  id_countries bigint,
  id_product_companies bigint
);

