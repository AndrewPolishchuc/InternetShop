package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.OrderDao;
import com.internet.shop.exception.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Order;
import com.internet.shop.model.Product;
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
public class OrderDaoJdbcImpl implements OrderDao {
    @Override
    public List<Order> getUserOrders(Long userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE user_id = ? AND deleted = FALSE;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to find users( with id -" + userId
                    + ") order" + userId, e);
        }
        for (Order order : orders) {
            order.setProducts(getProductFromOrder(order.getId()));
        }
        return orders;
    }

    @Override
    public Order create(Order item) {
        String query = "INSERT INTO orders(user_id) VALUES (?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query,
                         Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, item.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                item.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Order - " + item + " creation failed", e);
        }
        return productInsert(item);
    }

    @Override
    public Optional<Order> getById(Long item) {
        String query = "SELECT * FROM orders WHERE order_id = ? AND deleted = FALSE;";
        Order order = new Order();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, item);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                order = getOrderFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get" + item + "order", e);
        }
        order.setProducts(getProductFromOrder(order.getId()));
        return Optional.ofNullable(order);
    }

    @Override
    public Order update(Order item) {
        String queryToDeleteProducts = "DELETE FROM orders_products WHERE order_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(queryToDeleteProducts)) {
            statement.setLong(1, item.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("It is not possible to update the order " + item, e);
        }
        return productInsert(item);
    }

    @Override
    public boolean deleteById(Long item) {
        String query = "UPDATE orders SET deleted = true WHERE order_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, item);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("It is not possible to delete the order with ID - "
                    + item, e);
        }
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE deleted = FALSE;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to receive orders", e);
        }
        for (Order order : orders) {
            order.setProducts(getProductFromOrder(order.getId()));
        }
        return orders;
    }

    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException {
        Long orderId = resultSet.getLong("order_id");
        Long userId = resultSet.getLong("user_id");
        return new Order(orderId, userId);
    }

    private List<Product> getProductFromOrder(Long orderId) {
        String query = "SELECT * FROM products "
                + "INNER JOIN orders_products ON products.product_id = orders_products.product_id "
                + "WHERE orders_products.order_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, orderId);
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
            throw new DataProcessingException("Couldn't get order with ID = " + orderId, e);
        }
    }

    private Order productInsert(Order order) {
        String queryToUpdateProducts = "INSERT INTO orders_products(order_id, product_id) "
                + "VALUES(?,?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(queryToUpdateProducts)) {
            statement.setLong(1, order.getId());
            for (int i = 0; i < order.getProducts().size(); i++) {
                statement.setLong(2, order.getProducts().get(i).getId());
                statement.executeUpdate();
            }
            return order;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't insert data to order with ID = "
                    + order.getId(), e);
        }
    }

}
