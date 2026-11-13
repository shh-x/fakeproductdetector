package com.fakedetector.service;

import com.fakedetector.dao.ProductDAO;
import com.fakedetector.model.Product;
import com.fakedetector.util.DBConnection;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductDAO dao = new ProductDAO();

    public int create(Product p) throws SQLException {
        synchronized (DBConnection.LOCK) {
            return dao.insert(p);
        }
    }

    public List<Product> listFor(int manufacturerId) throws SQLException {
        synchronized (DBConnection.LOCK) {
            return dao.findByManufacturer(manufacturerId);
        }
    }

    /** Returns the product only if it belongs to this manufacturer, otherwise null. */
    public Product get(int productId, int manufacturerId) throws SQLException {
        synchronized (DBConnection.LOCK) {
            return dao.findByIdAndManufacturer(productId, manufacturerId);
        }
    }
}
