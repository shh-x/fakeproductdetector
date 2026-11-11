package com.fakedetector;

import com.fakedetector.dao.QRCodeDAO;
import com.fakedetector.model.QRCode;
import com.fakedetector.util.QRImageGenerator;
import com.fakedetector.util.TokenGenerator;

import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        try {
            QRCodeDAO qrDao = new QRCodeDAO();
            int productId = 1; // Must be a product that exists in your products table

            for (int i = 0; i < 3; i++) {
                String token = TokenGenerator.generate();
                int qrId = qrDao.insert(productId, token);
                
                // Generates the PNG file inside a folder named 'qrcodes'
                QRImageGenerator.generate(token, Paths.get("qrcodes", "qr_" + qrId + ".png"), 300);
                
                System.out.println("Created QR #" + qrId + " token=" + token);

                QRCode found = qrDao.findByToken(token);
                System.out.println("Looked up: " + found);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}