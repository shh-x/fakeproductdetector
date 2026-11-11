
package com.fakedetector.service;

import com.fakedetector.dao.QRCodeDAO;
import com.fakedetector.dao.ScanLogDAO;
import com.fakedetector.model.QRCode;
import com.fakedetector.model.QRStatus;
import com.fakedetector.model.VerificationResult;
import com.fakedetector.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class ScanService {
    private static final int SUSPICIOUS_THRESHOLD = 5;

    private final QRCodeDAO qrDao = new QRCodeDAO();
    private final ScanLogDAO logDao = new ScanLogDAO();

    public VerificationResult verify(String token, String ipAddress) throws SQLException {
        if (token == null || token.isBlank()) {
            return VerificationResult.COUNTERFEIT;
        }

        Connection con = DBConnection.getInstance().getConnection();
        boolean originalAutoCommit = con.getAutoCommit();

        try {
            con.setAutoCommit(false);                       // START TRANSACTION

            QRCode qr = qrDao.findByToken(token);
            if (qr == null) {
                con.rollback();                             // nothing to record
                return VerificationResult.COUNTERFEIT;
            }

            qrDao.incrementScanCount(qr.getQrId());         // change 1
            logDao.insert(qr.getQrId(), ipAddress);         // change 2
            QRCode updated = qrDao.findByToken(token);      // re-read the new count

            con.commit();                                   // COMMIT: both changes become permanent
            return classify(updated);

        } catch (SQLException e) {
            con.rollback();                                 // ROLLBACK: undo both
            throw e;
        } finally {
            con.setAutoCommit(originalAutoCommit);          // put the connection back as we found it
        }
    }

    // YOUR TURN
    
        private VerificationResult classify(QRCode qr) {
        if (qr.getStatus() == QRStatus.BLOCKED) {
            return VerificationResult.BLOCKED;
        } else if (qr.getScanCount() > SUSPICIOUS_THRESHOLD) {
            return VerificationResult.SUSPICIOUS;
        } else {
            return VerificationResult.GENUINE;
        }
    }
    }
