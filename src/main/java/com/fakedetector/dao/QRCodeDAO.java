package com.fakedetector.dao;

import com.fakedetector.model.ProductLabel;
import com.fakedetector.model.QRCode;
import com.fakedetector.model.QRStatus;
import com.fakedetector.util.DBConnection;
import com.fakedetector.model.ProductLabel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QRCodeDAO {

    public int insert(int productId, String token) throws SQLException {
        String sql = "INSERT INTO qr_codes (product_id, unique_token) VALUES (?, ?)";
        Connection con = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, productId);
            ps.setString(2, token);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    // Finds one QR code by its unique token, returns null if not found
    public QRCode findByToken(String token) throws SQLException {
        String sql = "SELECT qr_id, product_id, unique_token, scan_count, status, created_at " +
                     "FROM qr_codes WHERE unique_token = ?";
        Connection con = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Convert database status string to Java enum
                    QRStatus qrStatus = QRStatus.valueOf(rs.getString("status"));
                    
                    // Convert database timestamp to Java LocalDateTime
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                    return new QRCode(
                            rs.getInt("qr_id"),
                            rs.getInt("product_id"),
                            rs.getString("unique_token"),
                            rs.getInt("scan_count"),
                            qrStatus,
                            createdAt
                    );
                }
            }
        }
        return null; // Returns null if no matching token exists
    }

    // Finds all QR codes associated with a specific product, ordered by qr_id
    public List<QRCode> findByProduct(int productId) throws SQLException {
        String sql = "SELECT qr_id, product_id, unique_token, scan_count, status, created_at " +
                     "FROM qr_codes WHERE product_id = ? ORDER BY qr_id";
        Connection con = DBConnection.getInstance().getConnection();
        List<QRCode> qrCodes = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    QRStatus qrStatus = QRStatus.valueOf(rs.getString("status"));
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                    qrCodes.add(new QRCode(
                            rs.getInt("qr_id"),
                            rs.getInt("product_id"),
                            rs.getString("unique_token"),
                            rs.getInt("scan_count"),
                            qrStatus,
                            createdAt
                    ));
                }
            }
        }
        return qrCodes;
    }

    // Returns the token only if the QR belongs to a product of this manufacturer
    public String findTokenForManufacturer(int qrId, int manufacturerId) throws SQLException {
        String sql = "SELECT q.unique_token FROM qr_codes q "               
                   + "JOIN products p ON q.product_id = p.product_id "               
                   + "WHERE q.qr_id = ? AND p.manufacturer_id = ?";
        Connection con = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, qrId);
            ps.setInt(2, manufacturerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("unique_token");
                }
            }
        }
        return null;
    }
    
    public void incrementScanCount(int qrId) throws SQLException {
        String sql = "UPDATE qr_codes SET scan_count = scan_count + 1 WHERE qr_id = ?";
        Connection con = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, qrId);
            ps.executeUpdate();
        }
    }
    public ProductLabel findLabel(int qrId) throws SQLException {
    String sql = "SELECT p.name, m.company_name FROM qr_codes q "
               + "JOIN products p ON q.product_id = p.product_id "
               + "JOIN manufacturers m ON p.manufacturer_id = m.manufacturer_id "
               + "WHERE q.qr_id = ?";
    Connection con = DBConnection.getInstance().getConnection();
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, qrId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new ProductLabel(rs.getString("name"), rs.getString("company_name"));
            }
        }
    }
    return null;
}

}
