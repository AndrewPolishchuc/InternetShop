package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.service.OrderService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteOrderController extends HttpServlet {
    private static final Injector inject = Injector.getInstance("com.internet.shop");
    private OrderService orderService
            = (OrderService) inject.getInstance(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        orderService.delete(Long.valueOf(req.getParameter("orderId")));
        resp.sendRedirect(req.getContextPath() + "orders/all");
    }
}
