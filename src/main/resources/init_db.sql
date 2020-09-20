CREATE SCHEMA `internet_shop` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `internet_shop`.`products` (
  `product_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(225) NOT NULL,
  `price` DECIMAL(17,2) NOT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE INDEX `productscol_UNIQUE` (`name` ASC) VISIBLE);

INSERT INTO `internet_shop`.`products` (`name`, `price`) VALUES ('banana', '200');
INSERT INTO `internet_shop`.`products` (`name`, `price`) VALUES ('apple', '150');

ALTER TABLE `internet_shop`.`products`
ADD COLUMN `deleted` TINYINT(1) NULL DEFAULT 0 AFTER `price`;