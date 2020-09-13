package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Order;
import com.internet.shop.service.OrderService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetUserOrdersController extends HttpServlet {
    private static final long USER_ID = 1;
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private OrderService orderService
            = (OrderService) injector.getInstance(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Order> userOrders = orderService.getUserOrders(USER_ID);
        req.setAttribute("allOrders", userOrders);
        req.getRequestDispatcher("/WEB-INF/views/order/userOrders.jsp").forward(req, resp);
    }
}
