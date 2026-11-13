package com.fakedetector.service;

import com.fakedetector.dao.QRCodeDAO;
import com.fakedetector.dao.ScanLogDAO;
import com.fakedetector.model.*;
import com.fakedetector.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class ScanService {
    public static final int SUSPICIOUS_THRESHOLD = 5;                 // now public: Phase 7 reuses it
    private static final Pattern TOKEN_FORMAT = Pattern.compile("[0-9a-f]{32}");

    private final QRCodeDAO qrDao = new QRCodeDAO();
    private final ScanLogDAO logDao = new ScanLogDAO();

    public ScanOutcome verify(String token, String ipAddress) throws SQLException {
        if (token == null) {
            return ScanOutcome.counterfeit();
        }
        token = token.trim().toLowerCase();
        if (!TOKEN_FORMAT.matcher(token).matches()) {
            return ScanOutcome.counterfeit();                         // wrong shape: never ask the DB
        }
        synchronized (DBConnection.LOCK) {
            return doVerify(token, ipAddress);
        }
    }

    private ScanOutcome doVerify(String token, String ipAddress) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        boolean originalAutoCommit = con.getAutoCommit();
        try {
            con.setAutoCommit(false);

            QRCode qr = qrDao.findByToken(token);
            if (qr == null) {
                con.rollback();
                return ScanOutcome.counterfeit();
            }
            qrDao.incrementScanCount(qr.getQrId());
            logDao.insert(qr.getQrId(), ipAddress);
            QRCode updated = qrDao.findByToken(token);
            ProductLabel label = qrDao.findLabel(qr.getQrId());
            con.commit();

            return new ScanOutcome(classify(updated), label.productName(),
                                   label.companyName(), updated.getScanCount());
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(originalAutoCommit);
        }
    }

    private VerificationResult classify(QRCode qr) {
    // 1. If the QR code is blocked or deactivated by the manufacturer
    if (qr.getStatus() != null && "BLOCKED".equalsIgnoreCase(qr.getStatus().toString())) {
        return VerificationResult.BLOCKED;
    }
    
    // 2. If it has been scanned more times than the allowed threshold (5 scans)
    if (qr.getScanCount() > SUSPICIOUS_THRESHOLD) {
        return VerificationResult.SUSPICIOUS;
    }
    
    // 3. Otherwise, it is a valid, genuine product scan
    return VerificationResult.GENUINE;
}
}
