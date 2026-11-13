package com.fakedetector.servlet;

import com.fakedetector.model.Product;
import com.fakedetector.service.ProductService;
import com.fakedetector.service.QRService;
import com.fakedetector.util.Params;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/manufacturer/product")
public class ProductDetailServlet extends HttpServlet {
    private final ProductService products = new ProductService();
    private final QRService qrService = new QRService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int manufacturerId = (int) req.getSession().getAttribute("manufacturerId");
        int id = Params.toInt(req.getParameter("id"), -1);

        try {
            Product p = products.get(id, manufacturerId);
            if (p == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);   // missing OR not yours: same answer
                return;
            }
            req.setAttribute("product", p);
            req.setAttribute("qrCodes", qrService.listForProduct(id));
            req.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int manufacturerId = (int) req.getSession().getAttribute("manufacturerId");
        int id = Params.toInt(req.getParameter("id"), -1);
        int count = Params.toInt(req.getParameter("count"), 0);
        String base = req.getContextPath() + "/manufacturer/product?id=" + id;

        if (count < 1 || count > QRService.MAX_BATCH) {
            resp.sendRedirect(base + "&error=count");
            return;
        }
        try {
            int made = qrService.generateBatch(id, manufacturerId, count);
            if (made == 0) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            resp.sendRedirect(base + "&generated=" + made);       // Post-Redirect-Get again
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}