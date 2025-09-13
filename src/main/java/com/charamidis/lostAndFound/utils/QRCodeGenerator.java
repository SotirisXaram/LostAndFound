package com.charamidis.lostAndFound.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QRCodeGenerator {
    private static final Logger logger = AppLogger.getLogger();
    
    /**
     * Generate QR code as BufferedImage
     */
    public static BufferedImage generateQRCodeImage(String data, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
            
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            logger.log(Level.WARNING, "Error generating QR code", e);
            return null;
        }
    }
    
    /**
     * Generate QR code for a record that links to web display
     */
    public static BufferedImage generateRecordQRCode(String baseUrl, String recordUid, int width, int height) {
        String qrData = baseUrl + "/public/record/" + recordUid;
        return generateQRCodeImage(qrData, width, height);
    }
}
