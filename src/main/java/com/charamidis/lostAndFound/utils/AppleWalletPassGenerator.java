package com.charamidis.lostAndFound.utils;

import com.charamidis.lostAndFound.models.Record;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AppleWalletPassGenerator {
    
    private static final String PASS_TYPE_IDENTIFIER = "pass.com.charamidis.lostandfound";
    private static final String TEAM_IDENTIFIER = "YOUR_TEAM_ID"; // Replace with your Apple Developer Team ID
    private static final String ORGANIZATION_NAME = "Lost & Found System";
    private static final String PASS_DESCRIPTION = "Lost Item Record";
    
    static {
        // Add BouncyCastle provider
        Security.addProvider(new BouncyCastleProvider());
    }
    
    /**
     * Generate an Apple Wallet pass for a lost item record
     */
    public static byte[] generatePass(Record record, String baseUrl) throws Exception {
        // Create temporary directory for pass files
        File tempDir = Files.createTempDirectory("apple_wallet_pass").toFile();
        
        try {
            // Generate pass.json
            String passJson = generatePassJson(record, baseUrl);
            Files.write(Paths.get(tempDir.getPath(), "pass.json"), passJson.getBytes("UTF-8"));
            
            // Generate manifest.json
            String manifestJson = generateManifestJson(tempDir);
            Files.write(Paths.get(tempDir.getPath(), "manifest.json"), manifestJson.getBytes("UTF-8"));
            
            // Copy images if available
            if (record.getPicture_path() != null && !record.getPicture_path().isEmpty()) {
                File imageFile = new File(record.getPicture_path());
                if (imageFile.exists()) {
                    Files.copy(imageFile.toPath(), Paths.get(tempDir.getPath(), "icon.png"));
                    Files.copy(imageFile.toPath(), Paths.get(tempDir.getPath(), "logo.png"));
                }
            } else {
                // Generate default images
                generateDefaultImages(tempDir);
            }
            
            // Create the .pkpass file
            return createPkPassFile(tempDir);
            
        } finally {
            // Clean up temporary directory
            deleteDirectory(tempDir);
        }
    }
    
    private static String generatePassJson(Record record, String baseUrl) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"formatVersion\": 1,\n");
        json.append("  \"passTypeIdentifier\": \"").append(PASS_TYPE_IDENTIFIER).append("\",\n");
        json.append("  \"serialNumber\": \"").append(record.getUid()).append("\",\n");
        json.append("  \"teamIdentifier\": \"").append(TEAM_IDENTIFIER).append("\",\n");
        json.append("  \"organizationName\": \"").append(ORGANIZATION_NAME).append("\",\n");
        json.append("  \"description\": \"").append(PASS_DESCRIPTION).append("\",\n");
        json.append("  \"logoText\": \"").append(ORGANIZATION_NAME).append("\",\n");
        json.append("  \"foregroundColor\": \"rgb(255, 255, 255)\",\n");
        json.append("  \"backgroundColor\": \"rgb(60, 65, 76)\",\n");
        json.append("  \"labelColor\": \"rgb(255, 255, 255)\",\n");
        json.append("  \"barcode\": {\n");
        json.append("    \"message\": \"").append(baseUrl).append("/public/record/").append(record.getUid()).append("\",\n");
        json.append("    \"format\": \"PKBarcodeFormatQR\",\n");
        json.append("    \"messageEncoding\": \"iso-8859-1\"\n");
        json.append("  },\n");
        json.append("  \"generic\": {\n");
        json.append("    \"primaryFields\": [\n");
        json.append("      {\n");
        json.append("        \"key\": \"item\",\n");
        json.append("        \"label\": \"Item\",\n");
        json.append("        \"value\": \"").append(escapeJson(record.getItem_description() != null ? record.getItem_description() : "Lost Item")).append("\"\n");
        json.append("      }\n");
        json.append("    ],\n");
        json.append("    \"secondaryFields\": [\n");
        json.append("      {\n");
        json.append("        \"key\": \"found_date\",\n");
        json.append("        \"label\": \"Found Date\",\n");
        json.append("        \"value\": \"").append(record.getFound_date() != null ? record.getFound_date() : "Unknown").append("\"\n");
        json.append("      },\n");
        json.append("      {\n");
        json.append("        \"key\": \"found_time\",\n");
        json.append("        \"label\": \"Found Time\",\n");
        json.append("        \"value\": \"").append(record.getFound_time() != null ? record.getFound_time() : "Unknown").append("\"\n");
        json.append("      },\n");
        json.append("      {\n");
        json.append("        \"key\": \"location\",\n");
        json.append("        \"label\": \"Location\",\n");
        json.append("        \"value\": \"").append(escapeJson(record.getFound_location() != null ? record.getFound_location() : "Unknown")).append("\"\n");
        json.append("      }\n");
        json.append("    ],\n");
        json.append("    \"auxiliaryFields\": [\n");
        json.append("      {\n");
        json.append("        \"key\": \"record_id\",\n");
        json.append("        \"label\": \"Record ID\",\n");
        json.append("        \"value\": \"").append(record.getId()).append("\"\n");
        json.append("      },\n");
        json.append("      {\n");
        json.append("        \"key\": \"uid\",\n");
        json.append("        \"label\": \"UID\",\n");
        json.append("        \"value\": \"").append(record.getUid()).append("\"\n");
        json.append("      },\n");
        json.append("      {\n");
        json.append("        \"key\": \"status\",\n");
        json.append("        \"label\": \"Status\",\n");
        json.append("        \"value\": \"").append(record.getStatus() != null ? record.getStatus() : "stored").append("\"\n");
        json.append("      }\n");
        json.append("    ],\n");
        json.append("    \"backFields\": [\n");
        json.append("      {\n");
        json.append("        \"key\": \"founder_name\",\n");
        json.append("        \"label\": \"Founder\",\n");
        json.append("        \"value\": \"").append(escapeJson((record.getFounder_first_name() != null ? record.getFounder_first_name() : "") + " " + (record.getFounder_last_name() != null ? record.getFounder_last_name() : ""))).append("\"\n");
        json.append("      },\n");
        json.append("      {\n");
        json.append("        \"key\": \"founder_contact\",\n");
        json.append("        \"label\": \"Contact\",\n");
        json.append("        \"value\": \"").append(escapeJson(record.getFounder_telephone() != null ? record.getFounder_telephone() : "Not provided")).append("\"\n");
        json.append("      },\n");
        json.append("      {\n");
        json.append("        \"key\": \"item_category\",\n");
        json.append("        \"label\": \"Category\",\n");
        json.append("        \"value\": \"").append(escapeJson(record.getItem_category() != null ? record.getItem_category() : "Not specified")).append("\"\n");
        json.append("      },\n");
        json.append("      {\n");
        json.append("        \"key\": \"item_brand\",\n");
        json.append("        \"label\": \"Brand\",\n");
        json.append("        \"value\": \"").append(escapeJson(record.getItem_brand() != null ? record.getItem_brand() : "Not specified")).append("\"\n");
        json.append("      },\n");
        json.append("      {\n");
        json.append("        \"key\": \"item_model\",\n");
        json.append("        \"label\": \"Model\",\n");
        json.append("        \"value\": \"").append(escapeJson(record.getItem_model() != null ? record.getItem_model() : "Not specified")).append("\"\n");
        json.append("      },\n");
        json.append("      {\n");
        json.append("        \"key\": \"item_color\",\n");
        json.append("        \"label\": \"Color\",\n");
        json.append("        \"value\": \"").append(escapeJson(record.getItem_color() != null ? record.getItem_color() : "Not specified")).append("\"\n");
        json.append("      },\n");
        json.append("      {\n");
        json.append("        \"key\": \"item_serial\",\n");
        json.append("        \"label\": \"Serial Number\",\n");
        json.append("        \"value\": \"").append(escapeJson(record.getItem_serial_number() != null ? record.getItem_serial_number() : "Not specified")).append("\n");
        json.append("      },\n");
        json.append("      {\n");
        json.append("        \"key\": \"storage_location\",\n");
        json.append("        \"label\": \"Storage Location\",\n");
        json.append("        \"value\": \"").append(escapeJson(record.getStorage_location() != null ? record.getStorage_location() : "Not specified")).append("\"\n");
        json.append("      },\n");
        json.append("      {\n");
        json.append("        \"key\": \"web_url\",\n");
        json.append("        \"label\": \"Web URL\",\n");
        json.append("        \"value\": \"").append(baseUrl).append("/public/record/").append(record.getUid()).append("\"\n");
        json.append("      }\n");
        json.append("    ]\n");
        json.append("  }\n");
        json.append("}\n");
        
        return json.toString();
    }
    
    private static String generateManifestJson(File tempDir) throws IOException {
        Map<String, String> fileHashes = new HashMap<>();
        
        for (File file : tempDir.listFiles()) {
            if (file.isFile() && !file.getName().equals("manifest.json")) {
                String hash = calculateSHA1(file);
                fileHashes.put(file.getName(), hash);
            }
        }
        
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        boolean first = true;
        for (Map.Entry<String, String> entry : fileHashes.entrySet()) {
            if (!first) json.append(",\n");
            json.append("  \"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\"");
            first = false;
        }
        json.append("\n}\n");
        
        return json.toString();
    }
    
    private static String calculateSHA1(File file) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("SHA-1 algorithm not available", e);
        }
        
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        
        byte[] hash = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    private static void generateDefaultImages(File tempDir) throws IOException {
        // Create simple default images (this is a simplified version)
        // In a real implementation, you'd generate proper PNG images
        // For now, we'll create empty files as placeholders
        Files.write(Paths.get(tempDir.getPath(), "icon.png"), new byte[0]);
        Files.write(Paths.get(tempDir.getPath(), "logo.png"), new byte[0]);
    }
    
    private static byte[] createPkPassFile(File tempDir) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (File file : tempDir.listFiles()) {
                if (file.isFile()) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zos.putNextEntry(entry);
                    
                    try (FileInputStream fis = new FileInputStream(file)) {
                        IOUtils.copy(fis, zos);
                    }
                    
                    zos.closeEntry();
                }
            }
        }
        
        return baos.toByteArray();
    }
    
    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }
    
    private static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}
