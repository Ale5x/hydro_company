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
-- Table `hydro-company`.`companies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`companies` (
  `id_companies` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id_companies`))
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
  `id_companies` INT NOT NULL,
  `registration` DATETIME NOT NULL,
  `id_roles` INT NOT NULL,
  PRIMARY KEY (`id_users`),
  INDEX `fk_users_companies1_idx` (`id_companies` ASC) VISIBLE,
  INDEX `fk_users_roles1_idx` (`id_roles` ASC) VISIBLE,
  CONSTRAINT `fk_users_companies1`
    FOREIGN KEY (`id_companies`)
    REFERENCES `hydro-company`.`companies` (`id_companies`)
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
-- Table `hydro-company`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`products` (
  `id_products` INT NOT NULL AUTO_INCREMENT,
  `type` INT NOT NULL,
  `pressure` INT NOT NULL,
  `weight` DOUBLE NOT NULL,
  `path_hydraulic_scheme` VARCHAR(255) NOT NULL,
  `quantity_max` INT NOT NULL,
  `additional_inf` VARCHAR(45) NOT NULL,
  `id_products_type` INT NOT NULL,
  `count` INT NOT NULL,
  PRIMARY KEY (`id_products`),
  INDEX `fk_products_products_type1_idx` (`id_products_type` ASC) VISIBLE,
  CONSTRAINT `fk_products_products_type1`
    FOREIGN KEY (`id_products_type`)
    REFERENCES `hydro-company`.`products_type` (`id_products_type`)
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
-- Table `hydro-company`.`products_conection`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`products_conection` (
  `id_products_conection` INT NOT NULL AUTO_INCREMENT,
  `size` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_products_conection`))
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
-- Table `hydro-company`.`products_countries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`products_countries` (
  `id_products` INT NOT NULL,
  `id_countries` INT NOT NULL,
  PRIMARY KEY (`id_products`, `id_countries`),
  INDEX `fk_products_has_countries_countries1_idx` (`id_countries` ASC) VISIBLE,
  INDEX `fk_products_has_countries_products1_idx` (`id_products` ASC) VISIBLE,
  CONSTRAINT `fk_products_has_countries_products1`
    FOREIGN KEY (`id_products`)
    REFERENCES `hydro-company`.`products` (`id_products`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_has_countries_countries1`
    FOREIGN KEY (`id_countries`)
    REFERENCES `hydro-company`.`countries` (`id_countries`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`products_conection_has_products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`products_conection_has_products` (
  `id_products_conection` INT NOT NULL,
  `id_products` INT NOT NULL,
  PRIMARY KEY (`id_products_conection`, `id_products`),
  INDEX `fk_products_conection_has_products_products1_idx` (`id_products` ASC) VISIBLE,
  INDEX `fk_products_conection_has_products_products_conection1_idx` (`id_products_conection` ASC) VISIBLE,
  CONSTRAINT `fk_products_conection_has_products_products_conection1`
    FOREIGN KEY (`id_products_conection`)
    REFERENCES `hydro-company`.`products_conection` (`id_products_conection`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_conection_has_products_products1`
    FOREIGN KEY (`id_products`)
    REFERENCES `hydro-company`.`products` (`id_products`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`product_companies_has_products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`product_companies_has_products` (
  `id_product_companies` INT NOT NULL,
  `id_products` INT NOT NULL,
  PRIMARY KEY (`id_product_companies`, `id_products`),
  INDEX `fk_product_companies_has_products_products1_idx` (`id_products` ASC) VISIBLE,
  INDEX `fk_product_companies_has_products_product_companies1_idx` (`id_product_companies` ASC) VISIBLE,
  CONSTRAINT `fk_product_companies_has_products_product_companies1`
    FOREIGN KEY (`id_product_companies`)
    REFERENCES `hydro-company`.`product_companies` (`id_product_companies`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_companies_has_products_products1`
    FOREIGN KEY (`id_products`)
    REFERENCES `hydro-company`.`products` (`id_products`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`racks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`racks` (
  `id_racks` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_racks`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydro-company`.`products_racks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydro-company`.`products_racks` (
  `id_products` INT NOT NULL,
  `id_racks` INT NOT NULL,
  PRIMARY KEY (`id_products`, `id_racks`),
  INDEX `fk_products_has_racks_racks1_idx` (`id_racks` ASC) VISIBLE,
  INDEX `fk_products_has_racks_products1_idx` (`id_products` ASC) VISIBLE,
  CONSTRAINT `fk_products_has_racks_products1`
    FOREIGN KEY (`id_products`)
    REFERENCES `hydro-company`.`products` (`id_products`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_has_racks_racks1`
    FOREIGN KEY (`id_racks`)
    REFERENCES `hydro-company`.`racks` (`id_racks`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;



--- Data

INSERT INTO roles (id_roles, name) values (1, 'CEO'), (2, 'ADMIN'), (3, 'MANAGER'), (4, 'USER'), (5, 'CUSTOMER')