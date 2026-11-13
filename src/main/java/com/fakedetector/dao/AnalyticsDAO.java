package com.fakedetector.dao;

import com.fakedetector.model.*;
import com.fakedetector.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsDAO {

    public DashboardSummary summary(int manufacturerId, int threshold) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT p.product_id) AS products, "
                   + "COUNT(q.qr_id) AS qr_codes, "
                   + "COALESCE(SUM(q.scan_count), 0) AS total_scans, "
                   + "COALESCE(SUM(q.scan_count > ?), 0) AS suspicious, "
                   + "COALESCE(SUM(q.status = 'BLOCKED'), 0) AS blocked "
                   + "FROM products p "
                   + "LEFT JOIN qr_codes q ON q.product_id = p.product_id "
                   + "WHERE p.manufacturer_id = ?";
        Connection con = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            ps.setInt(2, manufacturerId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();   // an aggregate without GROUP BY always returns exactly one row
                return new DashboardSummary(rs.getInt("products"), rs.getInt("qr_codes"),
                        rs.getInt("total_scans"), rs.getInt("suspicious"), rs.getInt("blocked"));
            }
        }
    }

    public List<ProductStat> productStats(int manufacturerId) throws SQLException {
        String sql = "SELECT p.product_id, p.name, COUNT(q.qr_id) AS qr_codes, "
                   + "COALESCE(SUM(q.scan_count), 0) AS total_scans, "
                   + "COALESCE(MAX(q.scan_count), 0) AS max_scans "
                   + "FROM products p "
                   + "LEFT JOIN qr_codes q ON q.product_id = p.product_id "
                   + "WHERE p.manufacturer_id = ? "
                   + "GROUP BY p.product_id, p.name "
                   + "ORDER BY total_scans DESC";
        List<ProductStat> list = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, manufacturerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ProductStat(rs.getInt("product_id"), rs.getString("name"),
                            rs.getInt("qr_codes"), rs.getInt("total_scans"), rs.getInt("max_scans")));
                }
            }
        }
        return list;
    }

    public List<SuspiciousQR> suspicious(int manufacturerId, int threshold) throws SQLException {
        String sql = "SELECT q.qr_id, p.name, q.scan_count, q.status, "
                   + "COUNT(DISTINCT l.ip_address) AS distinct_ips, "
                   + "COALESCE(SUM(l.scan_timestamp >= NOW() - INTERVAL 1 DAY), 0) AS last_24h, "
                   + "MAX(l.scan_timestamp) AS last_scan "
                   + "FROM qr_codes q "
                   + "JOIN products p ON q.product_id = p.product_id "
                   + "LEFT JOIN scan_logs l ON l.qr_id = q.qr_id "
                   + "WHERE p.manufacturer_id = ? AND (q.scan_count > ? OR q.status = 'BLOCKED') "
                   + "GROUP BY q.qr_id, p.name, q.scan_count, q.status "
                   + "ORDER BY q.scan_count DESC "
                   + "LIMIT 50";
        List<SuspiciousQR> list = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, manufacturerId);
            ps.setInt(2, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("last_scan");
                    list.add(new SuspiciousQR(rs.getInt("qr_id"), rs.getString("name"),
                            rs.getInt("scan_count"), rs.getInt("distinct_ips"), rs.getInt("last_24h"),
                            ts == null ? null : ts.toLocalDateTime(),
                            "BLOCKED".equals(rs.getString("status"))));
                }
            }
        }
        return list;
    }

    public List<RecentScan> recentScans(int manufacturerId, int limit) throws SQLException {
        String sql = "SELECT l.scan_timestamp, p.name, l.qr_id, l.ip_address "
                   + "FROM scan_logs l "
                   + "JOIN qr_codes q ON l.qr_id = q.qr_id "
                   + "JOIN products p ON q.product_id = p.product_id "
                   + "WHERE p.manufacturer_id = ? "
                   + "ORDER BY l.scan_timestamp DESC "
                   + "LIMIT ?";
        List<RecentScan> list = new ArrayList<>();
        Connection con = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, manufacturerId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("scan_timestamp");
                    list.add(new RecentScan(
                            ts == null ? null : ts.toLocalDateTime(),
                            rs.getString("name"),
                            rs.getInt("qr_id"),
                            rs.getString("ip_address")
                    ));
                }
            }
        }
        return list;
    }
}