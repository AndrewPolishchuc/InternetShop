package com.internet.shop.dao;

import com.internet.shop.model.Order;
import com.internet.shop.model.ShoppingCart;
import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Order completeOrder(ShoppingCart shoppingCart);

    List<Order> getUserOrders(Long userId);

    Optional<Order> get(Long id);

    List<Order> getAll();

    boolean delete(Long id);

    Order create(Order order);

    Order update(Order order);
}
