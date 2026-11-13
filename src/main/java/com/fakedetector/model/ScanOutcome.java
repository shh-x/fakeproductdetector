package com.fakedetector.model;

public record ScanOutcome(VerificationResult result, String productName,
                          String companyName, int scanCount) {
    public static ScanOutcome counterfeit() {
        return new ScanOutcome(VerificationResult.COUNTERFEIT, null, null, 0);
    }
}
