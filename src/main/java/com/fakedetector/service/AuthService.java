package com.fakedetector.service;

import com.fakedetector.dao.ManufacturerDAO;
import com.fakedetector.model.Manufacturer;
import com.fakedetector.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class AuthService {
    private final ManufacturerDAO dao = new ManufacturerDAO();

    /** Returns the new Manufacturer, or null if the email is already registered. */
    public Manufacturer register(String company, String email, String password) throws SQLException {
        synchronized (DBConnection.LOCK) {
            if (dao.findByEmail(email) != null) {
                return null;
            }
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            try {
                int id = dao.insert(new Manufacturer(company, email, hash));
                return new Manufacturer(id, company, email, hash);
            } catch (SQLIntegrityConstraintViolationException e) {
                return null;   // the UNIQUE index on email caught a duplicate
            }
        }
    }

    /** Returns the Manufacturer if email and password match, otherwise null. */
    public Manufacturer login(String email, String password) throws SQLException {
        synchronized (DBConnection.LOCK) {
            Manufacturer m = dao.findByEmail(email);
            if (m == null) {
                return null;
            }
            return BCrypt.checkpw(password, m.getPasswordHash()) ? m : null;
        }
    }
}