package com.internet.shop.service;

import com.internet.shop.model.Order;
import java.util.List;

public interface OrderService {
    Order create(Order order);

    Order get(Long id);

    List<Order> getAll();

    Order update(Order order);

    boolean delete(Long id);
}
