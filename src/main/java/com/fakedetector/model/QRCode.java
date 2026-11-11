package com.fakedetector.model;

import java.time.LocalDateTime;

public class QRCode {
    // Private fields (Encapsulation)
    private int qrId;
    private int productId;
    private String uniqueToken;
    private int scanCount;
    private QRStatus status;
    private LocalDateTime createdAt;

    // Constructor taking all 6 values (used when reading from the database)
    public QRCode(int qrId, int productId, String uniqueToken, int scanCount, QRStatus status, LocalDateTime createdAt) {
        this.qrId = qrId;
        this.productId = productId;
        this.uniqueToken = uniqueToken;
        this.scanCount = scanCount;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getQrId() { return qrId; }
    public void setQrId(int qrId) { this.qrId = qrId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getUniqueToken() { return uniqueToken; }
    public void setUniqueToken(String uniqueToken) { this.uniqueToken = uniqueToken; }

    public int getScanCount() { return scanCount; }
    public void setScanCount(int scanCount) { this.scanCount = scanCount; }

    public QRStatus getStatus() { return status; }
    public void setStatus(QRStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "QRCode{id=" + qrId + ", productId=" + productId + ", token='" + uniqueToken 
                + "', scans=" + scanCount + ", status=" + status + ", created=" + createdAt + "}";
    }
}