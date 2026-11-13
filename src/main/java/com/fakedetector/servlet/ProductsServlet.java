package com.fakedetector.servlet;

import com.fakedetector.model.Product;
import com.fakedetector.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/manufacturer/products")
public class ProductsServlet extends HttpServlet {
    private final ProductService products = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        showPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int manufacturerId = (int) req.getSession().getAttribute("manufacturerId"); // from SESSION, never the form

        String name = req.getParameter("name") == null ? "" : req.getParameter("name").trim();
        String priceText = req.getParameter("price") == null ? "" : req.getParameter("price").trim();
        String dateText = req.getParameter("date") == null ? "" : req.getParameter("date").trim();

        String error = null;
        BigDecimal price = null;
        LocalDate date = null;

        if (name.isEmpty() || name.length() > 100) {
            error = "Product name is required (max 100 characters).";
        } else {
            try {
                price = new BigDecimal(priceText).setScale(2, RoundingMode.HALF_UP);
                if (price.signum() <= 0 || price.compareTo(new BigDecimal("99999999.99")) > 0) {
                    error = "Price must be greater than 0 and less than 100,000,000.";
                }
            } catch (NumberFormatException e) {
                error = "Price must be a number.";
            }
        }
        if (error == null && !dateText.isEmpty()) {
            try {
                date = LocalDate.parse(dateText);          // expects yyyy-MM-dd
            } catch (DateTimeParseException e) {
                error = "Date must look like 2026-03-10.";
            }
        }

        if (error == null) {
            try {
                int newId = products.create(new Product(name, price, date, manufacturerId));
                resp.sendRedirect(req.getContextPath() + "/manufacturer/product?id=" + newId);
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                error = "Something went wrong. Please try again.";
            }
        }
        req.setAttribute("error", error);
        req.setAttribute("name", name);
        req.setAttribute("price", priceText);
        req.setAttribute("date", dateText);
        showPage(req, resp);
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int manufacturerId = (int) req.getSession().getAttribute("manufacturerId");
        try {
            req.setAttribute("products", products.listFor(manufacturerId));
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Could not load products.");
        }
        req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, resp);
    }
}