package com.fakedetector.dao;

import com.fakedetector.model.QRCode;
import com.fakedetector.model.QRStatus;
import com.fakedetector.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

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

    // YOUR TURN: public QRCode findByToken(String token) throws SQLException
    // SQL: SELECT qr_id, product_id, unique_token, scan_count, status, created_at
    //      FROM qr_codes WHERE unique_token = ?
    // Two new conversions you need:
    //   status    -> QRStatus.valueOf(rs.getString("status"))
    //   created_at -> rs.getTimestamp("created_at").toLocalDateTime()
    // Return null if no row.
    



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
    
    public void incrementScanCount(int qrId) throws SQLException {
        String sql = "UPDATE qr_codes SET scan_count = scan_count + 1 WHERE qr_id = ?";
        Connection con = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, qrId);
            ps.executeUpdate();
        }
    }

}
