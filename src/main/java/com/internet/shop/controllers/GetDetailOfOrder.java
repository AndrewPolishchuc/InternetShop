package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Order;
import com.internet.shop.model.Product;
import com.internet.shop.service.OrderService;
import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetDetailOfOrder extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private OrderService orderService
            = (OrderService) injector.getInstance(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long orderId = Long.valueOf(req.getParameter("orderId"));
        Order order = orderService.get(orderId);
        req.setAttribute("order", order);
        BigDecimal currentSum = findCurrentSumOrder(orderId);
        req.setAttribute("currentSum", currentSum);
        req.getRequestDispatcher("/WEB-INF/views/order/detail.jsp").forward(req, resp);
    }

    private BigDecimal findCurrentSumOrder(Long userId) {
        double result = 0;
        for (Order order : orderService.getAll()) {
            for (Product orderProduct : order.getProducts()) {
                result += orderProduct.getPrice().doubleValue();
            }
        }
        return new BigDecimal(result);
    }
}
