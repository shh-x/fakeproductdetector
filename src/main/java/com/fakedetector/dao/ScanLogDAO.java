package com.fakedetector.dao;

import com.fakedetector.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ScanLogDAO {

    public void insert(int qrId, String ipAddress) throws SQLException {
        String sql = "INSERT INTO scan_logs (qr_id, ip_address) VALUES (?, ?)";
        Connection con = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, qrId);
            ps.setString(2, ipAddress);
            ps.executeUpdate();
        }
    }
}