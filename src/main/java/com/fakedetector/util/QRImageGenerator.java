package com.fakedetector.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QRImageGenerator {

    private QRImageGenerator() { }

    private static BitMatrix buildMatrix(String text, int sizePixels) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        return new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, sizePixels, sizePixels, hints);
    }

    public static void generate(String text, Path outputFile, int sizePixels)
            throws WriterException, IOException {
        Files.createDirectories(outputFile.getParent());
        MatrixToImageWriter.writeToPath(buildMatrix(text, sizePixels), "PNG", outputFile);
    }

    public static void writeTo(String text, OutputStream out, int sizePixels)
            throws WriterException, IOException {
        MatrixToImageWriter.writeToStream(buildMatrix(text, sizePixels), "PNG", out);
    }
}