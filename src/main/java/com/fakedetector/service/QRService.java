package com.fakedetector.service;

import com.fakedetector.dao.ProductDAO;
import com.fakedetector.dao.QRCodeDAO;
import com.fakedetector.model.QRCode;
import com.fakedetector.util.DBConnection;
import com.fakedetector.util.TokenGenerator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class QRService {
    public static final int MAX_BATCH = 100;

    private final ProductDAO productDao = new ProductDAO();
    private final QRCodeDAO qrDao = new QRCodeDAO();

    /** Creates `count` QR rows for the product. Returns how many were made (0 = not your product). */
    public int generateBatch(int productId, int manufacturerId, int count) throws SQLException {
        if (count < 1 || count > MAX_BATCH) {
            throw new IllegalArgumentException("count must be 1.." + MAX_BATCH);
        }
        synchronized (DBConnection.LOCK) {
            Connection con = DBConnection.getInstance().getConnection();
            boolean originalAutoCommit = con.getAutoCommit();
            try {
                con.setAutoCommit(false);                       // START TRANSACTION

                if (productDao.findByIdAndManufacturer(productId, manufacturerId) == null) {
                    con.rollback();
                    return 0;
                }
                for (int i = 0; i < count; i++) {
                    qrDao.insert(productId, TokenGenerator.generate());
                }
                con.commit();                                   // all `count` rows, or none
                return count;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(originalAutoCommit);
            }
        }
    }

    public List<QRCode> listForProduct(int productId) throws SQLException {
        synchronized (DBConnection.LOCK) {
            return qrDao.findByProduct(productId);
        }
    }

    public String tokenFor(int qrId, int manufacturerId) throws SQLException {
        synchronized (DBConnection.LOCK) {
            return qrDao.findTokenForManufacturer(qrId, manufacturerId);
        }
    }
}
