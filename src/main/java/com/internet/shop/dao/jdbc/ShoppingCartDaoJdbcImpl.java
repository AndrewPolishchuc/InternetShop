package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.ShoppingCartDao;
import com.internet.shop.exception.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.util.ConnectionUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class ShoppingCartDaoJdbcImpl implements ShoppingCartDao {
    @Override
    public Optional<ShoppingCart> getByUserId(Long userId) {
        ShoppingCart shoppingCart = null;
        String query = "SELECT * FROM shopping_carts WHERE user_id = ? AND deleted = FALSE;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                shoppingCart = getShoppingCartFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get shopping cart from DB with user ID = "
                    + userId, e);
        }
        shoppingCart.setProducts(getProductFromShoppingCart(shoppingCart.getId()));
        return Optional.ofNullable(shoppingCart);
    }

    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        String query = "INSERT INTO shopping_carts(user_id) VALUES (?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, shoppingCart.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                shoppingCart.setId(resultSet.getLong(1));
            }
            return shoppingCart;
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to create " + shoppingCart, e);
        }
    }

    @Override
    public Optional<ShoppingCart> getById(Long cartId) {
        ShoppingCart shoppingCart = null;
        String query = "SELECT * FROM shopping_carts WHERE cart_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, cartId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                shoppingCart = getShoppingCartFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("it is impossible to get a buyer's card"
                    + " with ID = " + cartId, e);
        }
        shoppingCart.setProducts(getProductFromShoppingCart(shoppingCart.getId()));
        return Optional.ofNullable(shoppingCart);
    }

    @Override
    public ShoppingCart update(ShoppingCart shoppingCart) {
        String queryToDeleteProducts = "DELETE FROM shopping_carts_products WHERE cart_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(queryToDeleteProducts)) {
            statement.setLong(1, shoppingCart.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to update a buyer card due to the inability"
                    + "to delete the old version - " + shoppingCart, e);
        }
        productInsert(shoppingCart);
        return shoppingCart;
    }

    @Override
    public boolean deleteById(Long cartId) {
        String query = "UPDATE shopping_carts SET deleted = true WHERE cart_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, cartId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("It is impossible to delete the buyer's card"
                    + cartId, e);
        }
    }

    @Override
    public List<ShoppingCart> getAll() {
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        String query = "SELECT * FROM shopping_carts WHERE deleted = false";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                shoppingCarts.add(getShoppingCartFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get shopping cart", e);
        }
        for (ShoppingCart shoppingCart : shoppingCarts) {
            shoppingCart.setProducts(getProductFromShoppingCart(shoppingCart.getId()));
        }
        return shoppingCarts;
    }

    private ShoppingCart getShoppingCartFromResultSet(ResultSet resultSet) throws SQLException {
        Long cartId = resultSet.getLong("cart_id");
        Long userId = resultSet.getLong("user_id");
        return new ShoppingCart(cartId, userId);
    }

    private List<Product> getProductFromShoppingCart(Long shoppingCartId) {
        String query = "SELECT * FROM products "
                + "INNER JOIN shopping_carts_products ON products.product_id = "
                + "shopping_carts_products.product_id "
                + "WHERE shopping_carts_products.cart_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, shoppingCartId);
            ResultSet resultSet = statement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Long productId = resultSet.getLong("product_id");
                String name = resultSet.getString("name");
                BigDecimal price = resultSet.getBigDecimal("price");
                products.add(new Product(productId, name, price));
            }
            return products;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get shopping cart with ID =" + shoppingCartId,
                    e);
        }
    }

    private ShoppingCart productInsert(ShoppingCart shoppingCart) {
        String query = "INSERT INTO "
                + "shopping_carts_products(cart_id, product_id) VALUES(?, ?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, shoppingCart.getId());
            for (Product product : shoppingCart.getProducts()) {
                statement.setLong(2, product.getId());
                statement.executeUpdate();
            }
            return shoppingCart;
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to update the buyer's card "
                    + shoppingCart, e);
        }
    }
}
