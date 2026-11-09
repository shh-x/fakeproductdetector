package com.fakedetector;

import com.fakedetector.dao.ProductDAO;
import com.fakedetector.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;

public class App {
    public static void main(String[] args) {
        try {
            ProductDAO dao = new ProductDAO();

            Product p = new Product("Cough Syrup", new BigDecimal("89.99"), LocalDate.of(2026, 3, 10), 1);
            int newId = dao.insert(p);
            System.out.println("Inserted with id: " + newId);

            Product found = dao.findById(newId);
            System.out.println("Read back: " + found);

            System.out.println("Missing one: " + dao.findById(99999));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
