DROP SCHEMA IF EXISTS hydrowarehouse CASCADE;
CREATE SCHEMA IF NOT EXISTS hydrowarehouse;
SET SCHEMA hydrowarehouse;

-- countries
CREATE TABLE countries (
  country_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL
);

-- user_companies
CREATE TABLE user_companies (
  user_company_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL,
  address VARCHAR(100) NOT NULL,
  country_id INT NOT NULL,
  FOREIGN KEY (country_id) REFERENCES countries(country_id) ON DELETE RESTRICT
);

-- roles
CREATE TABLE roles (
  role_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(15) NOT NULL
);

-- user_statuses
CREATE TABLE user_statuses (
  user_status_id INT AUTO_INCREMENT PRIMARY KEY,
  status VARCHAR(45) NOT NULL
);

-- users
CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  password VARCHAR(320) NOT NULL,
  path VARCHAR(255),
  user_company_id INT,
  registration TIMESTAMP NOT NULL,
  role_id INT NOT NULL,
  email VARCHAR(55) NOT NULL UNIQUE,
  user_status_id INT NOT NULL,
  FOREIGN KEY (user_company_id) REFERENCES user_companies(user_company_id) ON DELETE SET NULL,
  FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE RESTRICT,
  FOREIGN KEY (user_status_id) REFERENCES user_statuses(user_status_id) ON DELETE RESTRICT
);

-- products_type
CREATE TABLE products_type (
  product_type_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL
);

-- products_connections
CREATE TABLE products_connections (
  product_connection_id INT AUTO_INCREMENT PRIMARY KEY,
  size VARCHAR(45) NOT NULL
);

-- product_companies
CREATE TABLE product_companies (
  product_company_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL
);

-- products
CREATE TABLE products (
  product_id INT AUTO_INCREMENT PRIMARY KEY,
  flow_rate INT NOT NULL,
  pressure INT NOT NULL,
  weight DOUBLE NOT NULL,
  path_hydraulic_scheme VARCHAR(255) NOT NULL,
  pressure_max INT NOT NULL,
  additional_inf CLOB,
  product_type_id INT NOT NULL,
  count INT NOT NULL,
  product_connection_id INT NOT NULL,
  product_company_id INT NOT NULL,
  FOREIGN KEY (product_type_id) REFERENCES products_type(product_type_id) ON DELETE RESTRICT,
  FOREIGN KEY (product_connection_id) REFERENCES products_connections(product_connection_id) ON DELETE RESTRICT,
  FOREIGN KEY (product_company_id) REFERENCES product_companies(product_company_id) ON DELETE RESTRICT
);

-- pictures
CREATE TABLE pictures (
  picture_id INT AUTO_INCREMENT PRIMARY KEY,
  path VARCHAR(320) NOT NULL,
  product_id INT NOT NULL,
  FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- storage_racks
CREATE TABLE storage_racks (
  rack_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL
);

-- shelves
CREATE TABLE shelves (
  shelf_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(10) NOT NULL,
  storage_racks_id INT NOT NULL,
  FOREIGN KEY (storage_racks_id) REFERENCES storage_racks(rack_id) ON DELETE RESTRICT
);

-- countries_has_product_companies
CREATE TABLE countries_has_product_companies (
  country_id INT NOT NULL,
  product_company_id INT NOT NULL,
  PRIMARY KEY (country_id, product_company_id),
  FOREIGN KEY (country_id) REFERENCES countries(country_id) ON DELETE CASCADE,
  FOREIGN KEY (product_company_id) REFERENCES product_companies(product_company_id) ON DELETE CASCADE
);

-- product_sku_status
CREATE TABLE product_sku_status (
  product_sku_status_id INT AUTO_INCREMENT PRIMARY KEY,
  status VARCHAR(45) NOT NULL
);

-- product_sku
CREATE TABLE product_sku (
  product_sku_id INT AUTO_INCREMENT PRIMARY KEY,
  sku_code VARCHAR(55) NOT NULL,
  product_id INT NOT NULL,
  status_id INT NOT NULL,
  country_id INT NOT NULL,
  shelf_id INT NOT NULL,
  FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
  FOREIGN KEY (status_id) REFERENCES product_sku_status(product_sku_status_id) ON DELETE RESTRICT,
  FOREIGN KEY (country_id) REFERENCES countries(country_id) ON DELETE RESTRICT,
  FOREIGN KEY (shelf_id) REFERENCES shelves(shelf_id) ON DELETE RESTRICT
);