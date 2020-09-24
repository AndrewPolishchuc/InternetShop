package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.ProductDao;
import com.internet.shop.exception.DataProcessingException;
import com.internet.shop.lib.Dao;
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
public class ProductDaoJdbcImpl implements ProductDao {

    @Override
    public Product create(Product product) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String query = "INSERT INTO products(name, price) VALUES(?, ?)";
            PreparedStatement preparedStatement
                    = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                product.setId(resultSet.getLong(1));
            }
            return product;
        } catch (SQLException e) {
            throw new DataProcessingException("Product " + product + " was not created", e);
        }
    }

    @Override
    public Optional<Product> getById(Long productId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String query = "SELECT * FROM products WHERE product_id = ? AND deleted = FALSE";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getNewProduct(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Can`t get product with id - " + productId, e);
        }
    }

    @Override
    public Product update(Product product) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String query = "UPDATE products SET name = ?, price = ? WHERE product_id = ? "
                    + "AND deleted = FALSE";
            PreparedStatement preparedStatement
                    = connection.prepareStatement(query);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            preparedStatement.setLong(3, product.getId());
            preparedStatement.executeUpdate();
            return product;
        } catch (SQLException e) {
            throw new DataProcessingException("Product " + product + " has not been updated", e);
        }
    }

    @Override
    public boolean deleteById(Long productId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String query = "UPDATE products SET deleted = TRUE WHERE product_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, productId);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Product with id - " + productId
                    + " has not been deleted", e);
        }
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            String query = "SELECT * FROM products WHERE deleted = FALSE";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                products.add(getNewProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Exceptional receipt of all products", e);
        }
        return products;
    }

    private Product getNewProduct(ResultSet resultSet) throws SQLException {
        Long productId = resultSet.getLong("product_id");
        String name = resultSet.getString("name");
        BigDecimal price = resultSet.getBigDecimal("price");
        return new Product(productId, name, price);
    }
}
