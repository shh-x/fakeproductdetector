package com.fakedetector.servlet;

import com.fakedetector.service.AnalyticsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/manufacturer/dashboard")
public class DashboardServlet extends HttpServlet {
    private final AnalyticsService analytics = new AnalyticsService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int mid = (int) req.getSession().getAttribute("manufacturerId");
        try {
            req.setAttribute("summary", analytics.summary(mid));
            req.setAttribute("productStats", analytics.productStats(mid));
            req.setAttribute("suspicious", analytics.suspicious(mid));
            req.setAttribute("recentScans", analytics.recentScans(mid));
            req.setAttribute("threshold", analytics.threshold());
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Could not load analytics. Please try again.");
        }
        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}