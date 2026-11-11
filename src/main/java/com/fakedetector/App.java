package com.fakedetector;

import com.fakedetector.service.ScanService;

public class App {
    public static void main(String[] args) {
        try {
            ScanService service = new ScanService();
            String realToken = "03f28e9297b08afe6b146cf7ad6e214d" ;//change it back later

            for (int i = 1; i <= 7; i++) {
                System.out.println("Scan " + i + ": " + service.verify(realToken, "127.0.0.1"));
            }

            System.out.println("Fake token: " + service.verify("abc123", "127.0.0.1"));
            System.out.println("Empty token: " + service.verify("", "127.0.0.1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}