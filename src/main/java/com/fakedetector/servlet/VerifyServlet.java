package com.fakedetector.servlet;

import com.fakedetector.model.VerificationResult;
import com.fakedetector.service.ScanService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/verify")
public class VerifyServlet extends HttpServlet {

    private final ScanService scanService = new ScanService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        String ip = request.getRemoteAddr();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            VerificationResult result = scanService.verify(token, ip);
            response.getWriter().write("{\"result\":\"" + result.name() + "\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"result\":\"ERROR\"}");
        }
    }
}