-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema hydrowarehouse
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema hydrowarehouse
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `hydrowarehouse` DEFAULT CHARACTER SET utf8 ;
USE `hydrowarehouse` ;

-- -----------------------------------------------------
-- Table `hydrowarehouse`.`countries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`countries` (
  `country_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`country_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`user_companies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`user_companies` (
  `user_company_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `country_id` INT NOT NULL,
  PRIMARY KEY (`user_company_id`),
  INDEX `fk_user_company_countries1_idx` (`country_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_company_countries1`
    FOREIGN KEY (`country_id`)
    REFERENCES `hydrowarehouse`.`countries` (`country_id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`roles` (
  `role_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`role_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`user_statuses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`user_statuses` (
  `user_status_id` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`user_status_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(320) NOT NULL,
  `path` VARCHAR(255) NULL,
  `user_company_id` INT NULL,
  `registration` DATETIME NOT NULL,
  `role_id` INT NOT NULL,
  `email` VARCHAR(55) NOT NULL,
  `user_status_id` INT NOT NULL,
  PRIMARY KEY (`user_id`),
  INDEX `fk_users_roles1_idx` (`role_id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  INDEX `fk_users_companies1_idx` (`user_company_id` ASC) VISIBLE,
  INDEX `fk_users_user_statuses1_idx` (`user_status_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_companies1`
    FOREIGN KEY (`user_company_id`)
    REFERENCES `hydrowarehouse`.`user_companies` (`user_company_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_users_roles1`
    FOREIGN KEY (`role_id`)
    REFERENCES `hydrowarehouse`.`roles` (`role_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_users_user_statuses1`
    FOREIGN KEY (`user_status_id`)
    REFERENCES `hydrowarehouse`.`user_statuses` (`user_status_id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`products_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`products_type` (
  `product_type_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`product_type_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`products_connections`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`products_connections` (
  `product_connection_id` INT NOT NULL AUTO_INCREMENT,
  `size` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`product_connection_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`product_companies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`product_companies` (
  `product_company_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`product_company_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`products` (
  `product_id` INT NOT NULL AUTO_INCREMENT,
  `flow_rate` INT NOT NULL,
  `pressure` INT NOT NULL,
  `weight` DOUBLE NOT NULL,
  `path_hydraulic_scheme` VARCHAR(255) NOT NULL,
  `pressure_max` INT NOT NULL,
  `additional_inf` TEXT NULL,
  `product_type_id` INT NOT NULL,
  `count` INT NOT NULL,
  `product_connection_id` INT NOT NULL,
  `product_company_id` INT NOT NULL,
  PRIMARY KEY (`product_id`),
  INDEX `fk_products_products_type1_idx` (`product_type_id` ASC) VISIBLE,
  INDEX `fk_products_products_connection1_idx` (`product_connection_id` ASC) VISIBLE,
  INDEX `fk_products_product_companies1_idx` (`product_company_id` ASC) VISIBLE,
  CONSTRAINT `fk_products_products_type1`
    FOREIGN KEY (`product_type_id`)
    REFERENCES `hydrowarehouse`.`products_type` (`product_type_id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_products_connection1`
    FOREIGN KEY (`product_connection_id`)
    REFERENCES `hydrowarehouse`.`products_connections` (`product_connection_id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_product_companies1`
    FOREIGN KEY (`product_company_id`)
    REFERENCES `hydrowarehouse`.`product_companies` (`product_company_id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`pictures`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`pictures` (
  `picture_id` INT NOT NULL AUTO_INCREMENT,
  `path` VARCHAR(320) NOT NULL,
  `product_id` INT NOT NULL,
  PRIMARY KEY (`picture_id`),
  INDEX `fk_pictures_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_pictures_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `hydrowarehouse`.`products` (`product_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`storage_racks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`storage_racks` (
  `rack_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`rack_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`shelves`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`shelves` (
  `shelf_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(10) NOT NULL,
  `storage_racks_id` INT NOT NULL,
  PRIMARY KEY (`shelf_id`),
  INDEX `fk_shelf_storage_racks1_idx` (`storage_racks_id` ASC) VISIBLE,
  CONSTRAINT `fk_shelf_storage_racks1`
    FOREIGN KEY (`storage_racks_id`)
    REFERENCES `hydrowarehouse`.`storage_racks` (`rack_id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`countries_has_product_companies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`countries_has_product_companies` (
  `country_id` INT NOT NULL,
  `product_company_id` INT NOT NULL,
  INDEX `fk_countries_has_product_companies_product_companies1_idx` (`product_company_id` ASC) VISIBLE,
  INDEX `fk_countries_has_product_companies_countries1_idx` (`country_id` ASC) VISIBLE,
  PRIMARY KEY (`country_id`, `product_company_id`),
  CONSTRAINT `fk_countries_has_product_companies_countries1`
    FOREIGN KEY (`country_id`)
    REFERENCES `hydrowarehouse`.`countries` (`country_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_countries_has_product_companies_product_companies1`
    FOREIGN KEY (`product_company_id`)
    REFERENCES `hydrowarehouse`.`product_companies` (`product_company_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`product_sku_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`product_sku_status` (
  `product_sku_status_id` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`product_sku_status_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hydrowarehouse`.`product_sku`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hydrowarehouse`.`product_sku` (
  `product_sku_id` INT NOT NULL AUTO_INCREMENT,
  `sku_code` VARCHAR(55) NOT NULL,
  `product_id` INT NOT NULL,
  `status_id` INT NOT NULL,
  `country_id` INT NOT NULL,
  `shelf_id` INT NOT NULL,
  PRIMARY KEY (`product_sku_id`),
  INDEX `fk_product_sku_products1_idx` (`product_id` ASC) VISIBLE,
  INDEX `fk_product_sku_product_sku_status1_idx` (`status_id` ASC) VISIBLE,
  INDEX `fk_product_sku_countries1_idx` (`country_id` ASC) VISIBLE,
  INDEX `fk_product_sku_shelf1_idx` (`shelf_id` ASC) VISIBLE,
  CONSTRAINT `fk_product_sku_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `hydrowarehouse`.`products` (`product_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_sku_product_sku_status1`
    FOREIGN KEY (`status_id`)
    REFERENCES `hydrowarehouse`.`product_sku_status` (`product_sku_status_id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_sku_countries1`
    FOREIGN KEY (`country_id`)
    REFERENCES `hydrowarehouse`.`countries` (`country_id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_sku_shelf1`
    FOREIGN KEY (`shelf_id`)
    REFERENCES `hydrowarehouse`.`shelves` (`shelf_id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
