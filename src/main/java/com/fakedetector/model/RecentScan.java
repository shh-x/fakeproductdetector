package com.fakedetector.model;

import java.time.LocalDateTime;

public class RecentScan {
    private final LocalDateTime scannedAt;
    private final String productName;
    private final int qrId;
    private final String ipAddress;

    public RecentScan(LocalDateTime scannedAt, String productName, int qrId, String ipAddress) {
        this.scannedAt = scannedAt;
        this.productName = productName;
        this.qrId = qrId;
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getScannedAt() { return scannedAt; }
    public String getProductName() { return productName; }
    public int getQrId() { return qrId; }
    public String getIpAddress() { return ipAddress; }
}
