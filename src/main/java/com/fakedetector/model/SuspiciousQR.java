package com.fakedetector.model;

import java.time.LocalDateTime;

public class SuspiciousQR {
    private final int qrId;
    private final String productName;
    private final int scanCount;
    private final int distinctIps;
    private final int last24h;
    private final LocalDateTime lastScan;   // may be null
    private final boolean blocked;

    public SuspiciousQR(int qrId, String productName, int scanCount, int distinctIps,
                        int last24h, LocalDateTime lastScan, boolean blocked) {
        this.qrId = qrId;
        this.productName = productName;
        this.scanCount = scanCount;
        this.distinctIps = distinctIps;
        this.last24h = last24h;
        this.lastScan = lastScan;
        this.blocked = blocked;
    }
    public int getQrId()               { return qrId; }
    public String getProductName()     { return productName; }
    public int getScanCount()          { return scanCount; }
    public int getDistinctIps()        { return distinctIps; }
    public int getLast24h()            { return last24h; }
    public LocalDateTime getLastScan() { return lastScan; }
    public boolean isBlocked()         { return blocked; }
}
