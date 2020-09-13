package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Order;
import com.internet.shop.service.OrderService;
import com.internet.shop.service.ShoppingCartService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CompleteCurrentOrderController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private OrderService orderService
            = (OrderService) injector.getInstance(OrderService.class);
    private ShoppingCartService shoppingCartService
            = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Long cartId = Long.valueOf(req.getParameter("cartId"));
        Order order = orderService.completeOrder(shoppingCartService.getById(cartId));
        resp.sendRedirect(req.getContextPath() + "/user/orders");
    }
}
