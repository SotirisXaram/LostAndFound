package com.charamidis.lostAndFound.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.charamidis.lostAndFound.models.Record;

public class BarcodePrinter {
    private static final Logger logger = AppLogger.getLogger();

    public static void printBarcode(Record record) {
        if (record == null || record.getUid() == null || record.getUid().trim().isEmpty()) {
            logger.log(Level.WARNING, "Cannot print barcode: Record or UID is null/empty");
            return;
        }

        // Generate barcode image using the record's UID
        BufferedImage barcodeImage = generateBarcodeImage(record.getUid());

        if (barcodeImage != null) {
            // Print barcode image
            printBarcode(barcodeImage, record.getId(), record.getUid());
        } else {
            logger.log(Level.WARNING, "Failed to generate barcode image for record ID: " + record.getId());
        }
    }

    public static void printQRCode(Record record, String baseUrl) {
        if (record == null || record.getUid() == null || record.getUid().trim().isEmpty()) {
            logger.log(Level.WARNING, "Cannot print QR code: Record or UID is null/empty");
            return;
        }

        // Generate QR code image using the record's UID
        BufferedImage qrImage = generateQRCodeImage(record.getUid(), baseUrl);

        if (qrImage != null) {
            // Print QR code image
            printQRCode(qrImage, record.getId(), record.getUid());
        } else {
            logger.log(Level.WARNING, "Failed to generate QR code image for record ID: " + record.getId());
        }
    }

    public static void BarcodePrinter() {
        // Legacy method - kept for backward compatibility
        // Get the UID from the database (replace this with your actual method to retrieve the UID)
        String uid = getUIDFromDatabase();

        // Generate barcode image
        BufferedImage barcodeImage = generateBarcodeImage(uid);

        // Print barcode image
        printBarcode(barcodeImage, 0, uid);
    }

    private static String getUIDFromDatabase() {
        // Replace this with your actual method to retrieve the UID from the database
        // For example, you might use JDBC to connect to the database and execute a query
        return "123456789"; // Dummy UID for demonstration
    }

    private static BufferedImage generateBarcodeImage(String data) {
        try {
            // Create barcode writer
            Code128Writer barcodeWriter = new Code128Writer();
            // Encode the data as a barcode
            BitMatrix bitMatrix = barcodeWriter.encode(data, BarcodeFormat.CODE_128, 200, 50);

            // Convert the BitMatrix to a BufferedImage
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (Exception e) {
            logger.log(Level.WARNING,"Error Barcode while generate barcode image",e);
            return null;
        }
    }

    private static BufferedImage generateQRCodeImage(String uid, String baseUrl) {
        try {
            // Create QR code writer
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            // Create the URL for the public record page
            String qrData = baseUrl + "/public/record/" + uid;
            // Encode the data as a QR code
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, 300, 300);

            // Convert the BitMatrix to a BufferedImage
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error generating QR code image", e);
            return null;
        }
    }

    private static void printBarcode(BufferedImage barcodeImage, Integer recordId, String uid) {
        try {
            // Create export directory if it doesn't exist
            File exportDir = new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "export_data");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            
            // Save the barcode image to a file with record ID and UID
            String fileName = "barcode_R" + recordId + "_" + uid.substring(0, Math.min(8, uid.length())) + "_" + LocalDate.now() + ".png";
            File outputFile = new File(exportDir, fileName);
            ImageIO.write(barcodeImage, "png", outputFile);

            logger.log(Level.INFO, "Barcode saved to: " + outputFile.getAbsolutePath());
            
            // Open the image using the default image viewer
            Desktop.getDesktop().open(outputFile);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error Barcode in printBarcode", e);
        }
    }

    private static void printQRCode(BufferedImage qrImage, Integer recordId, String uid) {
        try {
            // Create export directory if it doesn't exist
            File exportDir = new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "export_data");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            
            // Save the QR code image to a file with record ID and UID
            String fileName = "qrcode_R" + recordId + "_" + uid.substring(0, Math.min(8, uid.length())) + "_" + LocalDate.now() + ".png";
            File outputFile = new File(exportDir, fileName);
            ImageIO.write(qrImage, "png", outputFile);

            logger.log(Level.INFO, "QR code saved to: " + outputFile.getAbsolutePath());
            
            // Open the image using the default image viewer
            Desktop.getDesktop().open(outputFile);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error QR code in printQRCode", e);
        }
    }
}
