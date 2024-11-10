-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema hydro-company
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema hydro-company
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `hydro-company` DEFAULT CHARACTER SET utf8 ;
USE `hydro-company` ;

-- -----------------------------------------------------
-- Table `hydro-company`.`user_company`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`user_company` (
  `id_user_company` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id_user_company`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`roles` (
  `id_roles` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id_roles`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`users` (
  `id_users` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(320) NOT NULL,
  `path` VARCHAR(255) NULL,
  `id_user_company` INT NOT NULL,
  `registration` DATETIME NOT NULL,
  `id_roles` INT NOT NULL,
  PRIMARY KEY (`id_users`),
  INDEX `fk_users_companies1_idx` (`id_user_company` ASC) VISIBLE,
  INDEX `fk_users_roles1_idx` (`id_roles` ASC) VISIBLE,
  CONSTRAINT `fk_users_companies1`
    FOREIGN KEY (`id_user_company`)
    REFERENCES `hydro-company`.`user_company` (`id_user_company`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_roles1`
    FOREIGN KEY (`id_roles`)
    REFERENCES `hydro-company`.`roles` (`id_roles`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`products_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`products_type` (
  `id_products_type` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_products_type`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`products_connection`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`products_connection` (
  `id_products_connection` INT NOT NULL AUTO_INCREMENT,
  `size` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_products_connection`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`product_companies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`product_companies` (
  `id_product_companies` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_product_companies`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`products` (
  `id_products` INT NOT NULL AUTO_INCREMENT,
  `sku` VARCHAR(45) NOT NULL,
  `flow_rate` INT NOT NULL,
  `pressure` INT NOT NULL,
  `weight` DOUBLE NOT NULL,
  `path_hydraulic_scheme` VARCHAR(255) NOT NULL,
  `pressure_max` INT NOT NULL,
  `additional_inf` VARCHAR(45) NOT NULL,
  `id_products_type` INT NOT NULL,
  `count` INT NOT NULL,
  `id_products_connection` INT NOT NULL,
  `id_product_companies` INT NOT NULL,
  PRIMARY KEY (`id_products`),
  INDEX `fk_products_products_type1_idx` (`id_products_type` ASC) VISIBLE,
  INDEX `fk_products_products_connection1_idx` (`id_products_connection` ASC) VISIBLE,
  INDEX `fk_products_product_companies1_idx` (`id_product_companies` ASC) VISIBLE,
  CONSTRAINT `fk_products_products_type1`
    FOREIGN KEY (`id_products_type`)
    REFERENCES `hydro-company`.`products_type` (`id_products_type`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_products_connection1`
    FOREIGN KEY (`id_products_connection`)
    REFERENCES `hydro-company`.`products_connection` (`id_products_connection`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_product_companies1`
    FOREIGN KEY (`id_product_companies`)
    REFERENCES `hydro-company`.`product_companies` (`id_product_companies`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`countries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`countries` (
  `id_countries` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_countries`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`pictures`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`pictures` (
  `id_pictures` INT NOT NULL AUTO_INCREMENT,
  `path` VARCHAR(320) NOT NULL,
  `id_products` INT NOT NULL,
  PRIMARY KEY (`id_pictures`),
  INDEX `fk_pictures_products1_idx` (`id_products` ASC) VISIBLE,
  CONSTRAINT `fk_pictures_products1`
    FOREIGN KEY (`id_products`)
    REFERENCES `hydro-company`.`products` (`id_products`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`products_countries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`products_countries` (
  `id_products` INT NOT NULL,
  `id_countries` INT NOT NULL,
  INDEX `fk_products_has_countries_products1_idx` (`id_products` ASC) VISIBLE,
  INDEX `fk_products_countries_countries1_idx` (`id_countries` ASC) VISIBLE,
  CONSTRAINT `fk_products_has_countries_products1`
    FOREIGN KEY (`id_products`)
    REFERENCES `hydro-company`.`products` (`id_products`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_countries_countries1`
    FOREIGN KEY (`id_countries`)
    REFERENCES `hydro-company`.`countries` (`id_countries`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`storage_racks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`storage_racks` (
  `id_racks` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_racks`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`storage_racks_products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`storage_racks_products` (
  `id_products` INT NOT NULL,
  `id_storage_racks` INT NOT NULL,
  PRIMARY KEY (`id_products`, `id_storage_racks`),
  INDEX `fk_products_has_racks_racks1_idx` (`id_storage_racks` ASC) VISIBLE,
  INDEX `fk_products_has_racks_products1_idx` (`id_products` ASC) VISIBLE,
  CONSTRAINT `fk_products_has_racks_products1`
    FOREIGN KEY (`id_products`)
    REFERENCES `hydro-company`.`products` (`id_products`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_has_racks_racks1`
    FOREIGN KEY (`id_storage_racks`)
    REFERENCES `hydro-company`.`storage_racks` (`id_racks`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`Shelf`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`Shelf` (
  `id_shelf` INT NOT NULL AUTO_INCREMENT,
  `number` INT NOT NULL,
  `id_storage_racks` INT NOT NULL,
  PRIMARY KEY (`id_shelf`),
  INDEX `fk_Shelf_storage_racks1_idx` (`id_storage_racks` ASC) VISIBLE,
  CONSTRAINT `fk_Shelf_storage_racks1`
    FOREIGN KEY (`id_storage_racks`)
    REFERENCES `hydro-company`.`storage_racks` (`id_racks`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`companies_has_countries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`companies_has_countries` (
  `id_companies` INT NOT NULL,
  `id_countries` INT NOT NULL,
  PRIMARY KEY (`id_companies`, `id_countries`),
  INDEX `fk_companies_has_countries_countries1_idx` (`id_countries` ASC) VISIBLE,
  INDEX `fk_companies_has_countries_companies1_idx` (`id_companies` ASC) VISIBLE,
  CONSTRAINT `fk_companies_has_countries_companies1`
    FOREIGN KEY (`id_companies`)
    REFERENCES `hydro-company`.`user_company` (`id_user_company`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_companies_has_countries_countries1`
    FOREIGN KEY (`id_countries`)
    REFERENCES `hydro-company`.`countries` (`id_countries`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`countries_has_product_companies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`countries_has_product_companies` (
  `id_countries` INT NOT NULL,
  `id_product_companies` INT NOT NULL,
  INDEX `fk_countries_has_product_companies_product_companies1_idx` (`id_product_companies` ASC) VISIBLE,
  INDEX `fk_countries_has_product_companies_countries1_idx` (`id_countries` ASC) VISIBLE,
  CONSTRAINT `fk_countries_has_product_companies_countries1`
    FOREIGN KEY (`id_countries`)
    REFERENCES `hydro-company`.`countries` (`id_countries`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_countries_has_product_companies_product_companies1`
    FOREIGN KEY (`id_product_companies`)
    REFERENCES `hydro-company`.`product_companies` (`id_product_companies`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


--- Data

INSERT INTO roles (id_roles, name) values (1, 'CEO'), (2, 'ADMIN'), (3, 'MANAGER'), (4, 'USER'), (5, 'CUSTOMER');
insert into countries(name) values('USA'), ('Belarus'), ('Canada');
insert into user_company(name, address) values('BOSH', 'Street 1'), ('AlG', 'Street AlG');
insert into companies_has_countries(id_companies, id_countries) values(1, 1), (2,2);
Insert into users (first_name, last_name, registration, password, path, id_user_company, id_roles, email)
value('Postman firstName 22', 'Postman lastName 2', '2017-06-15',
'$2a$10$85fO2hl1JE3mLTNGa7/rquyyr8m7KAN.vxBXJ96Ck/A15JAg2sP/6', 'Postman photo', '1', '1', 'admin@gmail.com');
