package com.charamidis.lostAndFound.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Resources {
    private static final Logger logger = AppLogger.getLogger();

    public static void initData() {
        String fullpath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "export_data";
        if (Files.notExists(Path.of(fullpath))) {
            try {
                Files.createDirectory(Path.of(fullpath));
            } catch (IOException exception) {
              logger.log(Level.SEVERE,"Cant make directory export_data in desktop",exception);
            }
        }
    }


    public static File getFile(String resourceName) {
        return new File(System.getProperty("user.dir") + File.separator + resourceName);
    }

    public static String getFileFromJAR(String resourceName) {
        resourceName = resourceName.replace("\\", "/");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try (InputStream stream = classLoader.getResourceAsStream(resourceName);
             Scanner sc = stream != null ? new Scanner(stream) : null) {

            if (sc != null) {
                sc.useDelimiter("\\A");
                return sc.hasNext() ? sc.next() : "";
            } else {
                // Handle the case where the resource is not found
                System.err.println("Resource not found: " + resourceName);
                return "";
            }

        } catch (Exception e) {
           logger.log(Level.SEVERE,"Get file from jar fail",e);
            return "";
        }
    }
}




