package com.fakedetector.servlet;

import com.fakedetector.model.ScanOutcome;
import com.fakedetector.service.ScanService;
import com.google.gson.JsonObject;

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getParameter("token");
        String ip = req.getRemoteAddr();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-store");

        JsonObject json = new JsonObject();
        try {
            ScanOutcome o = scanService.verify(token, ip);
            json.addProperty("result", o.result().name());
            if (o.productName() != null) {
                json.addProperty("productName", o.productName());
                json.addProperty("companyName", o.companyName());
                json.addProperty("scanCount", o.scanCount());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            json.addProperty("result", "ERROR");
        }
        resp.getWriter().write(json.toString());
    }
}