package com.internet.shop.service;

import com.internet.shop.model.ShoppingCart;
import java.util.List;

public interface ChoppingCardService {
    ShoppingCart create(ShoppingCart shoppingCart);

    ShoppingCart get(Long id);

    List<ShoppingCart> getAll();

    ShoppingCart update(ShoppingCart shoppingCart);

    boolean delete(Long id);
}
