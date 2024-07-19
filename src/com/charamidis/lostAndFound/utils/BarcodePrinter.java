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

public class BarcodePrinter {
    private static final Logger logger = AppLogger.getLogger();

    public static void BarcodePrinter() {
        // Get the UID from the database (replace this with your actual method to retrieve the UID)
        String uid = getUIDFromDatabase();

        // Generate barcode image
        BufferedImage barcodeImage = generateBarcodeImage(uid);

        // Print barcode image
        printBarcode(barcodeImage);
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

    private static void printBarcode(BufferedImage barcodeImage) {
        try {
            // Save the barcode image to a file
            File outputFile = new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"export_data"+File.separator+"barcode_"+LocalDate.now()+".png");
            ImageIO.write(barcodeImage, "png", outputFile);

            // Open the image using the default image viewer
            Desktop.getDesktop().open(outputFile);
        } catch (Exception e) {
            logger.log(Level.WARNING,"Error Barcode in printBarcode",e);

        }
    }
}
