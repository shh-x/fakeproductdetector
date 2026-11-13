package com.fakedetector.model;

public class DashboardSummary {
    private final int products;
    private final int qrCodes;
    private final int totalScans;
    private final int suspicious;
    private final int blocked;

    public DashboardSummary(int products, int qrCodes, int totalScans, int suspicious, int blocked) {
        this.products = products;
        this.qrCodes = qrCodes;
        this.totalScans = totalScans;
        this.suspicious = suspicious;
        this.blocked = blocked;
    }
    public int getProducts()   { return products; }
    public int getQrCodes()    { return qrCodes; }
    public int getTotalScans() { return totalScans; }
    public int getSuspicious() { return suspicious; }
    public int getBlocked()    { return blocked; }
}
