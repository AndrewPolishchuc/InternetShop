package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.model.Role;
import com.internet.shop.model.User;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InjectDataController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private final ProductService product
            = (ProductService) injector.getInstance(ProductService.class);
    private final UserService userService
            = (UserService) injector.getInstance(UserService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User alisa = new User("alisa","alisa", "12345");
        alisa.setRoles(Set.of(Role.of("USER")));
        userService.create(alisa);
        User bob = new User("bob","bob", "12345");
        bob.setRoles(Set.of(Role.of("ADMIN")));
        userService.create(bob);
        req.getRequestDispatcher("/WEB-INF/views/user/InjectData.jsp").forward(req, resp);
    }
}
