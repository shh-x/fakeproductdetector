package com.fakedetector.model;

public class ProductStat {
    private final int productId;
    private final String name;
    private final int qrCodes;
    private final int totalScans;
    private final int maxScans;

    public ProductStat(int productId, String name, int qrCodes, int totalScans, int maxScans) {
        this.productId = productId;
        this.name = name;
        this.qrCodes = qrCodes;
        this.totalScans = totalScans;
        this.maxScans = maxScans;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public int getQrCodes() { return qrCodes; }
    public int getTotalScans() { return totalScans; }
    public int getMaxScans() { return maxScans; }
}
