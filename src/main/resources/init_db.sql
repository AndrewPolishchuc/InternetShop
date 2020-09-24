CREATE SCHEMA `internet_shop` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `internet_shop`.`products` (
  `product_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(225) NOT NULL,
  `price` DECIMAL(17,2) NOT NULL,
  PRIMARY KEY (`product_id`);

INSERT INTO `internet_shop`.`products` (`name`, `price`) VALUES ('banana', '200');
INSERT INTO `internet_shop`.`products` (`name`, `price`) VALUES ('apple', '150');

ALTER TABLE `internet_shop`.`products`
ADD COLUMN `deleted` TINYINT(1) NULL DEFAULT 0 AFTER `price`;

CREATE TABLE `internet_shop`.`users` (
  `user_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(225) NOT NULL,
  `login` VARCHAR(225) NOT NULL,
  `password` VARCHAR(225) NOT NULL,
  `deleted` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`user_id`));

CREATE TABLE `internet_shop`.`roles` (
  `role_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(255) NOT NULL,
  `deleted` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`role_id`));


CREATE TABLE `internet_shop`.`users_roles` (
  `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(11) NOT NULL,
  `role_id` BIGINT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_id_idx` (`user_id` ASC) VISIBLE,
  INDEX `role_id_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `internet_shop`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `role_id`
    FOREIGN KEY (`role_id`)
    REFERENCES `internet_shop`.`roles` (`role_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE `internet_shop`.`shopping_carts` (
  `cart_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(11) NOT NULL,
  `deleted` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`cart_id`),
  INDEX `user_id_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `user_id_SC`
    FOREIGN KEY (`user_id`)
    REFERENCES `internet_shop`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `internet_shop`.`shopping_carts_products` (
  `id` BIGINT(11) NOT NULL,
  `cart_id` BIGINT(11) NOT NULL,
  `product_id` BIGINT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `cart_id_SCP_idx` (`cart_id` ASC) VISIBLE,
  INDEX `product_id_SCP_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `cart_id_SCP`
    FOREIGN KEY (`cart_id`)
    REFERENCES `internet_shop`.`shopping_carts` (`cart_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `product_id_SCP`
    FOREIGN KEY (`product_id`)
    REFERENCES `internet_shop`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `internet_shop`.`orders` (
  `order_id` BIGINT(11) NOT NULL,
  `user_id` BIGINT(11) NOT NULL,
  `deleted` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`order_id`),
  INDEX `user_id_Or_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `user_id_Or`
    FOREIGN KEY (`user_id`)
    REFERENCES `internet_shop`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `internet_shop`.`orders_products` (
  `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT(11) NOT NULL,
  `product_id` BIGINT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `order_id_OP_idx` (`order_id` ASC) VISIBLE,
  INDEX `product_id_OP_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `order_id_OP`
    FOREIGN KEY (`order_id`)
    REFERENCES `internet_shop`.`orders` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `product_id_OP`
    FOREIGN KEY (`product_id`)
    REFERENCES `internet_shop`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
