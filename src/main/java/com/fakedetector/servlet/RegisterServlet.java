package com.fakedetector.servlet;

import com.fakedetector.model.Manufacturer;
import com.fakedetector.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final AuthService auth = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String company = trim(req.getParameter("company"));
        String email = trim(req.getParameter("email")).toLowerCase();
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");

        String error = null;
        if (company.isEmpty() || company.length() > 100) {
            error = "Company name is required (max 100 characters).";
        } else if (!email.contains("@") || email.length() > 100) {
            error = "Please enter a valid email.";
        } else if (password.length() < 8) {
            error = "Password must be at least 8 characters.";
        }

        if (error == null) {
            try {
                Manufacturer m = auth.register(company, email, password);
                if (m == null) {
                    error = "That email is already registered.";
                } else {
                    resp.sendRedirect(req.getContextPath() + "/login?registered=1");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                error = "Something went wrong. Please try again.";
            }
        }

        req.setAttribute("error", error);
        req.setAttribute("company", company);
        req.setAttribute("email", email);
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}