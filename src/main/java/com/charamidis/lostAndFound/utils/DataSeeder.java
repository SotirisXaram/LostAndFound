package com.charamidis.lostAndFound.utils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import org.mindrot.jbcrypt.BCrypt;

public class DataSeeder {
    
    private static final String[] FIRST_NAMES = {
        "Γιάννης", "Μαρία", "Κώστας", "Ελένη", "Νίκος", "Αννα", "Δημήτρης", "Σοφία",
        "Αλέξανδρος", "Χριστίνα", "Μιχάλης", "Δέσποινα", "Αντώνης", "Αγγελική", "Παύλος", "Ευαγγελία",
        "Σπύρος", "Κατερίνα", "Θεόδωρος", "Αικατερίνη", "Γεώργιος", "Ειρήνη", "Αθανάσιος", "Παρασκευή",
        "Βασίλης", "Αναστασία", "Χαράλαμπος", "Ευδοκία", "Ιωάννης", "Αγγελική", "Πέτρος", "Μαρία"
    };
    
    private static final String[] LAST_NAMES = {
        "Παπαδόπουλος", "Γεωργίου", "Δημητρίου", "Κωνσταντίνου", "Αντωνίου", "Νικολάου",
        "Παπαγιάννη", "Γεωργιάδου", "Δημητριάδου", "Κωνσταντίνου", "Αντωνιάδου", "Νικολάου",
        "Παπαδάκης", "Γεωργάκης", "Δημητράκης", "Κωνσταντάκης", "Αντωνάκης", "Νικολάκης",
        "Παπαγιάννης", "Γεωργιάδης", "Δημητριάδης", "Κωνσταντάδης", "Αντωνιάδης", "Νικολάδης"
    };
    
    private static final String[] STREETS = {
        "Λεωφόρος Αθηνών", "Οδός Σολωμού", "Πλατεία Συντάγματος", "Οδός Πατησίων", "Λεωφόρος Κηφισίας",
        "Οδός Ακαδημίας", "Πλατεία Ομονοίας", "Οδός Ερμού", "Λεωφόρος Βασιλίσσης Σοφίας", "Οδός Πανεπιστημίου",
        "Πλατεία Μοναστηρακιού", "Οδός Αδριανού", "Λεωφόρος Συγγρού", "Οδός Διονυσίου Αρεοπαγίτου", "Πλατεία Κλαυθμώνος"
    };
    
    private static final String[] AREAS = {
        "Αθήνα", "Θεσσαλονίκη", "Πάτρα", "Ηράκλειο", "Λάρισα", "Βόλος", "Ιωάννινα", "Καβάλα",
        "Σέρρες", "Χανιά", "Κατερίνη", "Τρίκαλα", "Λαμία", "Κομοτηνή", "Δράμα", "Αλεξανδρούπολη"
    };
    
    private static final String[] ITEM_CATEGORIES = {
        "Ταυτότητα", "Διαβατήριο", "Πορτοφόλι", "Κλειδιά", "Τηλέφωνο", "Τσάντα", "Ρολόι", "Γυαλιά",
        "Βιβλίο", "Αυτοκίνητο", "Ποδήλατο", "Καπέλο", "Σακίδιο", "Ακουστικά", "Φλας", "Καμμέρα"
    };
    
    private static final String[] ITEM_BRANDS = {
        "Apple", "Samsung", "Nike", "Adidas", "Sony", "Canon", "Nikon", "Ray-Ban", "Rolex", "Omega",
        "Gucci", "Prada", "Louis Vuitton", "Chanel", "Dior", "Hermes", "Versace", "Armani", "Calvin Klein", "Hugo Boss"
    };
    
    private static final String[] ITEM_COLORS = {
        "Μαύρο", "Λευκό", "Κόκκινο", "Μπλε", "Πράσινο", "Κίτρινο", "Μωβ", "Πορτοκαλί", "Ροζ", "Καφέ",
        "Γκρι", "Ασημί", "Χρυσό", "Αργυρό", "Μπεζ", "Τυρκουάζ", "Μπορντό", "Μαρούν", "Κυανό", "Βιολέ"
    };
    
    private static final String[] LOCATIONS = {
        "Σταθμός Μετρό Συντάγματος", "Αεροδρόμιο Ελευθέριος Βενιζέλος", "Λιμάνι Πειραιά", "Σταθμός Λαρίσης",
        "Πανεπιστήμιο Αθηνών", "Νοσοκομείο Ευαγγελισμός", "Σχολή Αθηνών", "Πάρκο Εθνικού Κήπου",
        "Αγορά Μοναστηρακιού", "Σταθμός Ομόνοια", "Πλατεία Κλαυθμώνος", "Σταθμός Ακαδημίας",
        "Πανεπιστήμιο Θεσσαλονίκης", "Σταθμός Θεσσαλονίκης", "Αεροδρόμιο Μακεδονία", "Λιμάνι Θεσσαλονίκης"
    };
    
    private static final String[] STORAGE_LOCATIONS = {
        "Αποθήκη Α", "Αποθήκη Β", "Αποθήκη Γ", "Αποθήκη Δ", "Αποθήκη Ε", "Αποθήκη ΣΤ", "Αποθήκη Ζ", "Αποθήκη Η",
        "Κεντρικό Γραφείο", "Υποκατάστημα 1", "Υποκατάστημα 2", "Υποκατάστημα 3", "Υποκατάστημα 4", "Υποκατάστημα 5"
    };
    
    private static final String[] RETURN_STATUSES = {
        "Επιστράφηκε", "Σε αναμονή", "Αδύνατη επιστροφή", "Καταστράφηκε", "Δωρεά"
    };
    
    private static final Random random = new Random();
    
    public static void seedDatabase(Connection connection) {
        try {
            System.out.println("🌱 Starting database seeding...");
            
            // Clear existing data
            clearExistingData(connection);
            
            // Seed users
            seedUsers(connection);
            
            // Seed records
            seedRecords(connection);
            
            // Seed returns
            seedReturns(connection);
            
            System.out.println("✅ Database seeding completed successfully!");
            System.out.println("📊 Seeded data:");
            System.out.println("   - Users: 10");
            System.out.println("   - Records: 1000");
            System.out.println("   - Returns: 500");
            
        } catch (SQLException e) {
            System.err.println("❌ Error seeding database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void clearExistingData(Connection connection) throws SQLException {
        System.out.println("🧹 Clearing existing data...");
        
        try (Statement stmt = connection.createStatement()) {
            // Check if tables exist before trying to clear them
            try {
                stmt.executeUpdate("DELETE FROM returns");
            } catch (SQLException e) {
                // Table doesn't exist, skip
            }
            
            try {
                stmt.executeUpdate("DELETE FROM records");
            } catch (SQLException e) {
                // Table doesn't exist, skip
            }
            
            try {
                stmt.executeUpdate("DELETE FROM users WHERE am != 287874"); // Keep admin user
            } catch (SQLException e) {
                // Table doesn't exist, skip
            }
            
            try {
                stmt.executeUpdate("DELETE FROM activities");
            } catch (SQLException e) {
                // Table doesn't exist, skip
            }
        }
    }
    
    private static void seedUsers(Connection connection) throws SQLException {
        System.out.println("👥 Seeding users...");
        
        String insertUser = "INSERT INTO users (am, password, first_name, last_name, role) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(insertUser)) {
            for (int i = 1; i <= 10; i++) {
                stmt.setInt(1, 100000 + i);
                stmt.setString(2, BCrypt.hashpw("password123", BCrypt.gensalt()));
                stmt.setString(3, FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]);
                stmt.setString(4, LAST_NAMES[random.nextInt(LAST_NAMES.length)]);
                stmt.setString(5, i == 1 ? "admin" : "user");
                stmt.executeUpdate();
            }
        }
    }
    
    private static void seedRecords(Connection connection) throws SQLException {
        System.out.println("📝 Seeding records...");
        
        String insertRecord = "INSERT INTO records (" +
            "record_datetime, officer_id, founder_last_name, founder_first_name, " +
            "founder_id_number, founder_telephone, founder_street_address, founder_street_number, " +
            "founder_father_name, founder_area_inhabitant, found_date, found_time, " +
            "found_location, item_description, item_category, item_brand, item_model, " +
            "item_color, item_serial_number, storage_location" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(insertRecord)) {
            for (int i = 1; i <= 1000; i++) {
                // Record datetime (random date in last 2 years)
                LocalDateTime recordDateTime = LocalDateTime.now()
                    .minusDays(random.nextInt(730))
                    .minusHours(random.nextInt(24))
                    .minusMinutes(random.nextInt(60));
                stmt.setString(1, recordDateTime.toString());
                
                // Officer ID (random user)
                stmt.setInt(2, 100000 + random.nextInt(10) + 1);
                
                // Founder details
                stmt.setString(3, LAST_NAMES[random.nextInt(LAST_NAMES.length)]);
                stmt.setString(4, FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]);
                stmt.setString(5, generateIdNumber());
                stmt.setString(6, generatePhoneNumber());
                stmt.setString(7, STREETS[random.nextInt(STREETS.length)]);
                stmt.setString(8, String.valueOf(random.nextInt(200) + 1));
                stmt.setString(9, FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]);
                stmt.setString(10, AREAS[random.nextInt(AREAS.length)]);
                
                // Found date and time
                LocalDate foundDate = recordDateTime.toLocalDate().minusDays(random.nextInt(30));
                LocalTime foundTime = LocalTime.of(random.nextInt(24), random.nextInt(60));
                stmt.setString(11, foundDate.toString());
                stmt.setString(12, foundTime.toString());
                
                // Item details
                stmt.setString(13, LOCATIONS[random.nextInt(LOCATIONS.length)]);
                stmt.setString(14, generateItemDescription());
                stmt.setString(15, ITEM_CATEGORIES[random.nextInt(ITEM_CATEGORIES.length)]);
                stmt.setString(16, ITEM_BRANDS[random.nextInt(ITEM_BRANDS.length)]);
                stmt.setString(17, generateModel());
                stmt.setString(18, ITEM_COLORS[random.nextInt(ITEM_COLORS.length)]);
                stmt.setString(19, generateSerialNumber());
                stmt.setString(20, STORAGE_LOCATIONS[random.nextInt(STORAGE_LOCATIONS.length)]);
                
                stmt.executeUpdate();
                
                if (i % 100 == 0) {
                    System.out.println("   📝 Seeded " + i + " records...");
                }
            }
        }
    }
    
    private static void seedReturns(Connection connection) throws SQLException {
        System.out.println("🔄 Seeding returns...");
        
        // First, get all record IDs
        String getRecordIds = "SELECT id FROM records ORDER BY RANDOM() LIMIT 500";
        int[] recordIds = new int[500];
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(getRecordIds)) {
            int index = 0;
            while (rs.next() && index < 500) {
                recordIds[index++] = rs.getInt("id");
            }
        }
        
        String insertReturn = "INSERT INTO returns (" +
            "return_officer, return_last_name, return_first_name, return_date, return_time, " +
            "return_telephone, return_id_number, return_father_name, return_date_of_birth, " +
            "return_street_address, return_street_number, comment, record_id" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(insertReturn)) {
            for (int i = 0; i < 500; i++) {
                // Return officer (random user)
                stmt.setInt(1, 100000 + random.nextInt(10) + 1);
                
                // Return person details
                stmt.setString(2, LAST_NAMES[random.nextInt(LAST_NAMES.length)]);
                stmt.setString(3, FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]);
                
                // Return date and time
                LocalDate returnDate = LocalDate.now().minusDays(random.nextInt(365));
                LocalTime returnTime = LocalTime.of(random.nextInt(24), random.nextInt(60));
                stmt.setString(4, returnDate.toString());
                stmt.setString(5, returnTime.toString());
                
                // Contact details
                stmt.setString(6, generatePhoneNumber());
                stmt.setString(7, generateIdNumber());
                stmt.setString(8, FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]);
                stmt.setString(9, LocalDate.now().minusYears(random.nextInt(50) + 18).toString());
                
                // Address details
                stmt.setString(10, STREETS[random.nextInt(STREETS.length)]);
                stmt.setString(11, String.valueOf(random.nextInt(200) + 1));
                
                // Comment
                String status = RETURN_STATUSES[random.nextInt(RETURN_STATUSES.length)];
                stmt.setString(12, generateReturnNotes(status));
                
                // Record ID
                stmt.setInt(13, recordIds[i]);
                
                stmt.executeUpdate();
                
                if ((i + 1) % 50 == 0) {
                    System.out.println("   🔄 Seeded " + (i + 1) + " returns...");
                }
            }
        }
    }
    
    private static String generateIdNumber() {
        return String.format("%d%06d", random.nextInt(2) + 1, random.nextInt(1000000));
    }
    
    private static String generatePhoneNumber() {
        return String.format("69%08d", random.nextInt(100000000));
    }
    
    private static String generateItemDescription() {
        String[] adjectives = {"Μικρό", "Μεγάλο", "Παλιό", "Καινούριο", "Ακριβό", "Φτηνό", "Ξύλινο", "Μεταλλικό", "Πλαστικό", "Δερμάτινο"};
        String[] items = {"αντικείμενο", "πράγμα", "στοιχείο", "προϊόν", "εξάρτημα", "αξεσουάρ", "εργαλείο", "συσκευή", "όργανο", "μηχάνημα"};
        
        return adjectives[random.nextInt(adjectives.length)] + " " + items[random.nextInt(items.length)] + 
               " με " + ITEM_COLORS[random.nextInt(ITEM_COLORS.length)].toLowerCase() + " χρώμα";
    }
    
    private static String generateModel() {
        String[] modelPrefixes = {"Pro", "Max", "Ultra", "Elite", "Premium", "Standard", "Basic", "Advanced", "Classic", "Modern"};
        return modelPrefixes[random.nextInt(modelPrefixes.length)] + "-" + (random.nextInt(9999) + 1000);
    }
    
    private static String generateSerialNumber() {
        return String.format("SN%08d", random.nextInt(100000000));
    }
    
    private static String generateReturnNotes(String status) {
        switch (status) {
            case "Επιστράφηκε":
                return "Το αντικείμενο επιστράφηκε στον νόμιμο ιδιοκτήτη μετά από επαλήθευση ταυτότητας.";
            case "Σε αναμονή":
                return "Το αντικείμενο βρίσκεται σε αναμονή για επιστροφή. Αναμένεται επικοινωνία με τον ιδιοκτήτη.";
            case "Αδύνατη επιστροφή":
                return "Δεν ήταν δυνατή η επιστροφή λόγω ελλιπών στοιχείων ή αδυναμίας επικοινωνίας με τον ιδιοκτήτη.";
            case "Καταστράφηκε":
                return "Το αντικείμενο καταστράφηκε κατά τη διάρκεια της φύλαξης λόγω φυσικής φθοράς ή ατυχήματος.";
            case "Δωρεά":
                return "Το αντικείμενο δωρήθηκε σε φιλανθρωπικό ίδρυμα μετά από την παρέλευση του νόμιμου χρόνου φύλαξης.";
            default:
                return "Σημείωμα επιστροφής: " + status;
        }
    }
}
