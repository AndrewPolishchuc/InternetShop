package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.model.User;
import com.internet.shop.service.OrderService;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;
import java.math.BigDecimal;

public class Application {
    private static Injector injector = Injector.getInstance("com.internet.shop");
    private static final Long TROUSERS_INDEX = 2L;
    private static final Long SHORTS_INDEX = 1L;
    private static final Long T_SHORTS_INDEX = 3L;

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);
        productService.create(new Product("Shorts", 150.99));
        productService.create(new Product("Trousers", 450.99));
        productService.create(new Product("T-shirt", 219.99));
        for (Product product : productService.getAll()) {
            System.out.println(product);
        }
        System.out.println("Changing price of trousers:");
        Product trousersProduct = productService.get(TROUSERS_INDEX);
        trousersProduct.setPrice(new BigDecimal("599.99"));
        productService.update(trousersProduct);
        System.out.println(productService.get(TROUSERS_INDEX));
        System.out.println("Deleting T - shorts:");
        productService.delete(T_SHORTS_INDEX);
        for (Product product : productService.getAll()) {
            System.out.println(product);
        }
        System.out.println("Users:");
        UserService userService = (UserService) injector.getInstance(UserService.class);
        User bob = userService.create(new User("Bob", "bob_here", "123454321"));
        User alisa = userService.create(new User("Alisa", "alisa_here", "543212345"));
        System.out.println(userService.getAll());
        System.out.println("Carts:");
        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        ShoppingCart shoppingBobCart =
                shoppingCartService.create(new ShoppingCart(bob.getId()));
        ShoppingCart shoppingAlisaCart =
                shoppingCartService.create(new ShoppingCart(alisa.getId()));
        shoppingCartService.addProduct(shoppingBobCart, productService.get(TROUSERS_INDEX));
        shoppingCartService.addProduct(shoppingAlisaCart, productService.get(SHORTS_INDEX));
        System.out.println("Bob`s cart:");
        System.out.println(shoppingBobCart.getProducts());
        System.out.println("Alisa`s cart:");
        System.out.println(shoppingAlisaCart.getProducts());
        System.out.println("Deleting Alisa's item from the cart");
        shoppingCartService.deleteProduct(shoppingAlisaCart, productService.get(SHORTS_INDEX));
        System.out.println("Alisa`s cart:");
        System.out.println(shoppingAlisaCart.getProducts());
        System.out.println("Order:");
        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        orderService.completeOrder(shoppingBobCart);
        orderService.completeOrder(shoppingAlisaCart);
        System.out.println(orderService.get(bob.getId()));
        System.out.println(orderService.get(alisa.getId()));
        orderService.delete(bob.getId());
        System.out.println("Order after deleting Bob's cart:");
        System.out.println(orderService.getAll());
    }
}
