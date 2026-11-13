package com.fakedetector.servlet;

import com.fakedetector.model.Manufacturer;
import com.fakedetector.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService auth = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email") == null ? "" : req.getParameter("email").trim().toLowerCase();
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");

        try {
            Manufacturer m = auth.login(email, password);
            if (m == null) {
                req.setAttribute("error", "Invalid email or password.");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }

            HttpSession old = req.getSession(false);
            if (old != null) {
                old.invalidate();                       // throw away any old wristband
            }
            HttpSession session = req.getSession(true); // hand out a brand-new one
            session.setAttribute("manufacturerId", m.getManufacturerId());
            session.setAttribute("companyName", m.getCompanyName());
            resp.sendRedirect(req.getContextPath() + "/manufacturer/dashboard");

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Something went wrong. Please try again.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}
