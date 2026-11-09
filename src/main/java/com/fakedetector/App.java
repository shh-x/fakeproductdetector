package com.fakedetector;

import com.fakedetector.dao.ManufacturerDAO;
import com.fakedetector.model.Manufacturer;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLIntegrityConstraintViolationException;

public class App {
    public static void main(String[] args) {
        try {
            ManufacturerDAO dao = new ManufacturerDAO();

            String hash = BCrypt.hashpw("secret123", BCrypt.gensalt());
            System.out.println("Hash 1: " + hash);
            System.out.println("Hash 2: " + BCrypt.hashpw("secret123", BCrypt.gensalt()));

            try {
                int id = dao.insert(new Manufacturer("Beta Foods", "beta@example.com", hash));
                System.out.println("Inserted id: " + id);
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("❌ Email already registered! Please use a different email.");
            }

            Manufacturer found = dao.findByEmail("beta@example.com");
            System.out.println("Found: " + found);

            if (found != null) {
                System.out.println("Right password: " + BCrypt.checkpw("secret123", found.getPasswordHash()));
                System.out.println("Wrong password: " + BCrypt.checkpw("wrong", found.getPasswordHash()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
