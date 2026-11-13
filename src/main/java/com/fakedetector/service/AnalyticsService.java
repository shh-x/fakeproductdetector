package com.fakedetector.service;

import com.fakedetector.dao.AnalyticsDAO;
import com.fakedetector.dao.QRCodeDAO;
import com.fakedetector.model.*;
import com.fakedetector.util.DBConnection;

import java.sql.SQLException;
import java.util.List;

public class AnalyticsService {
    private final AnalyticsDAO analytics = new AnalyticsDAO();
    private final QRCodeDAO qrDao = new QRCodeDAO();
    private static final int T = ScanService.SUSPICIOUS_THRESHOLD;   // ONE definition of "suspicious"

    public int threshold() { return T; }

    public DashboardSummary summary(int mid) throws SQLException {
        synchronized (DBConnection.LOCK) { return analytics.summary(mid, T); }
    }
    public List<ProductStat> productStats(int mid) throws SQLException {
        synchronized (DBConnection.LOCK) { return analytics.productStats(mid); }
    }
    public List<SuspiciousQR> suspicious(int mid) throws SQLException {
        synchronized (DBConnection.LOCK) { return analytics.suspicious(mid, T); }
    }
    public List<RecentScan> recentScans(int mid) throws SQLException {
        synchronized (DBConnection.LOCK) { return analytics.recentScans(mid, 20); }
    }
    public boolean setStatus(int qrId, int mid, QRStatus status) throws SQLException {
        synchronized (DBConnection.LOCK) { return qrDao.setStatusForManufacturer(qrId, mid, status); }
    }
}
