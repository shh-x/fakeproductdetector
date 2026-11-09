package com.fakedetector.dao;

import com.fakedetector.model.Product;
import com.fakedetector.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductDAO {

    // Saves a product, returns the new id MySQL generated
    public int insert(Product p) throws SQLException {
        String sql = "INSERT INTO products (name, price, manufacture_date, manufacturer_id) VALUES (?, ?, ?, ?)";
        Connection con = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName());
            ps.setBigDecimal(2, p.getPrice());
            ps.setDate(3, p.getManufactureDate() == null ? null : Date.valueOf(p.getManufactureDate()));
            ps.setInt(4, p.getManufacturerId());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    // Finds one product by id, returns null if there isn't one
    public Product findById(int id) throws SQLException {
        String sql = "SELECT product_id, name, price, manufacture_date, manufacturer_id FROM products WHERE product_id = ?";
        Connection con = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Date d = rs.getDate("manufacture_date");
                    return new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getBigDecimal("price"),
                            d == null ? null : d.toLocalDate(),
                            rs.getInt("manufacturer_id"));
                }
            }
        }
        return null;
    }
}