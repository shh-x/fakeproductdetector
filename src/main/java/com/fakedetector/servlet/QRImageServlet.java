package com.fakedetector.servlet;

import com.fakedetector.service.QRService;
import com.fakedetector.util.Params;
import com.fakedetector.util.QRImageGenerator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/manufacturer/qr-image")
public class QRImageServlet extends HttpServlet {
    private final QRService qrService = new QRService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int manufacturerId = (int) req.getSession().getAttribute("manufacturerId");
        int qrId = Params.toInt(req.getParameter("id"), -1);

        try {
            String token = qrService.tokenFor(qrId, manufacturerId);
            if (token == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            resp.setContentType("image/png");
            resp.setHeader("Cache-Control", "private, no-store");
            QRImageGenerator.writeTo(token, resp.getOutputStream(), 300);
        } catch (SQLException | com.google.zxing.WriterException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
