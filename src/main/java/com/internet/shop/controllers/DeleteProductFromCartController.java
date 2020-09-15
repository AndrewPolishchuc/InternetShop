package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteProductFromCartController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private final ProductService productService
            = (ProductService) injector.getInstance(ProductService.class);
    private final ShoppingCartService shoppingCartService
            = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Long productId = Long.valueOf(req.getParameter("productId"));
        Long userId = (Long) req.getSession().getAttribute(AddProductToCartController.USER_ID);
        ShoppingCart shoppingCart = shoppingCartService.getByUserId(userId);
        shoppingCartService.deleteProduct(shoppingCart, productService.get(productId));
        resp.sendRedirect(req.getContextPath() + "/cart");
    }
}
