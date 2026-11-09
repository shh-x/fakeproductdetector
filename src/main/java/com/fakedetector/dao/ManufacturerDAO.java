package com.fakedetector.dao;

import com.fakedetector.model.Manufacturer;
import com.fakedetector.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManufacturerDAO {

    public int insert(Manufacturer m) throws SQLException {
        String sql = "INSERT INTO manufacturers (company_name, email, password_hash) VALUES (?, ?, ?)";
        Connection con = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getCompanyName());
            ps.setString(2, m.getEmail());
            ps.setString(3, m.getPasswordHash());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    // Finds one manufacturer by email, returns null if not found
    public Manufacturer findByEmail(String email) throws SQLException {
        String sql = "SELECT manufacturer_id, company_name, email, password_hash FROM manufacturers WHERE email = ?";
        Connection con = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Fill the ? blank with the email string passed into the method
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                // If a matching row exists in the database
                if (rs.next()) {
                    return new Manufacturer(
                            rs.getInt("manufacturer_id"),
                            rs.getString("company_name"),
                            rs.getString("email"),
                            rs.getString("password_hash")
                    );
                }
            }
        }
        // If no matching manufacturer was found, return null
        return null;
    }
}
