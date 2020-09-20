package com.internet.shop;

import com.internet.shop.dao.jdbc.ProductDaoJdbcImpl;
import com.internet.shop.model.Product;

public class Application {

    public static void main(String[] args) {
        ProductDaoJdbcImpl dao = new ProductDaoJdbcImpl();
        Product orange = new Product("orange",180);
        Product pineapple = new Product("pineapple",300);
        System.out.println(dao.getAll());
        //System.out.println(dao.deleteById(1L));
        System.out.println(dao.getById(13L));
        System.out.println(dao.getById(2L));
        //dao.create(orange);
        //dao.create(pineapple);
        System.out.println(dao.getAll());
    }
}
