package com.fakedetector.util;

import java.security.SecureRandom;
import java.util.HexFormat;

public class TokenGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    private TokenGenerator() { }   // utility class: nobody should create objects of it

    public static String generate() {
        byte[] bytes = new byte[16];          // 16 bytes = 128 bits of randomness
        RANDOM.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes); // 16 bytes -> 32 hex characters
    }
}
