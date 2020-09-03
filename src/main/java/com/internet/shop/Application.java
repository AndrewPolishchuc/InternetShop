package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.service.ProductService;

import java.math.BigDecimal;

public class Application {
    private static Injector injector = Injector.getInstance("com.internet.shop");
    private static final Long TROUSERS_ID = 2L;
    private static final Long SHORTS = 1L;

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);
        productService.create(new Product("Shorts", 150.99));
        productService.create(new Product("Trousers", 450.99));
        productService.create(new Product("T-shirt", 219.99));
        for (Product product : productService.getAll()) {
            System.out.println(product);
        }
        System.out.println("Changing price of trousers:");
        Product trousersProduct = productService.get(TROUSERS_ID);
        trousersProduct.setPrice(new BigDecimal("599.99"));
        productService.update(trousersProduct);
        System.out.println(productService.get(TROUSERS_ID));

        System.out.println("Deleting shorts:");
        productService.delete(SHORTS);
        for (Product product : productService.getAll()) {
            System.out.println(product);
        } 
    }
}
