package com.fakedetector.servlet;

import com.fakedetector.model.QRStatus;
import com.fakedetector.service.AnalyticsService;
import com.fakedetector.util.Params;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/manufacturer/qr-status")
public class QRStatusServlet extends HttpServlet {
    private final AnalyticsService analytics = new AnalyticsService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int mid = (int) req.getSession().getAttribute("manufacturerId");
        int qrId = Params.toInt(req.getParameter("id"), -1);
        String action = req.getParameter("action");

        QRStatus target;
        if ("block".equals(action)) {
            target = QRStatus.BLOCKED;
        } else if ("unblock".equals(action)) {
            target = QRStatus.ACTIVE;
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            if (!analytics.setStatus(qrId, mid, target)) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);       // missing OR not yours
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/manufacturer/dashboard?updated=" + action);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
