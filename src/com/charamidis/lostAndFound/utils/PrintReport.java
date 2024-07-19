package com.charamidis.lostAndFound.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.charamidis.lostAndFound.models.Record;
import org.apache.poi.xwpf.usermodel.*;
import java.io.OutputStream;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PrintReport {
        Connection connection;
         private static final Logger logger = Logger.getLogger(AppLogger.class.getName());

        public PrintReport(Record record, Connection conn){
        connection = conn;
        Locale greekLocale = new Locale("el", "GR");
        XWPFDocument document = new XWPFDocument();
        String filePathApodixi = "Τριπλότυπη απόδειξη";
        File outputFile = new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"export_data"+File.separator+filePathApodixi+"_"+record.getId()+".docx");

        try(OutputStream out = new FileOutputStream(outputFile)){

            XWPFParagraph paragraph1 = document.createParagraph();
            paragraph1.setAlignment(ParagraphAlignment.RIGHT);
            XWPFRun run1 = paragraph1.createRun();
            run1.setText("ΑΠΟΚΟΜΜΑ Α'");
            run1.setBold(true);

            XWPFParagraph paragraph2 = document.createParagraph();
            XWPFRun run2Left = paragraph2.createRun();
            run2Left.setText("ΔΑΑΑ/ΤΑ");
            paragraph2.createRun().addTab();
            paragraph2.createRun().addTab();
            paragraph2.createRun().addTab();
            paragraph2.createRun().addTab();
            paragraph2.createRun().addTab();
            paragraph2.createRun().addTab();
            XWPFRun run2Right = paragraph2.createRun();
            run2Right.setText("Αυξ. αριθ. τριπλ. αποδείξης .....................");
            run2Right.setUnderline(UnderlinePatterns.SINGLE);
            paragraph2.setAlignment(ParagraphAlignment.BOTH);

            XWPFParagraph paragraph3 = document.createParagraph();
            XWPFRun run3Left = paragraph3.createRun();
            run3Left.setText("Γραφείο Απολεσθέντων");
            paragraph3.createRun().addTab();
            paragraph3.createRun().addTab();
            paragraph3.createRun().addTab();
            paragraph3.createRun().addTab();
            XWPFRun run3Right = paragraph3.createRun();
            run3Right.setText("Αυξ. αριθ. καταχ. στο βιβλ. Απολλ. "+record.getId().toString().trim());
            run3Right.setUnderline(UnderlinePatterns.SINGLE);
            run3Right.addTab();

            paragraph3.setAlignment(ParagraphAlignment.BOTH);

            XWPFParagraph titleParagraph = document.createParagraph();
            titleParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setBold(true);
            titleRun.setUnderline(UnderlinePatterns.SINGLE);
            titleRun.setFontSize(14);
            titleRun.setText("ΤΡΙΠΛΟΤΥΠΗ ΑΠΟΔΕΙΞΗ");

            String officerName = "";
            DayOfWeek day = LocalDate.parse(record.getRecord_date()).getDayOfWeek();
            Month month = LocalDate.parse(record.getRecord_date()).getMonth();
            Year year = Year.of(LocalDate.parse(record.getRecord_date()).getYear());
           try{
               Statement idStm = connection.createStatement();
               ResultSet resultSet = idStm.executeQuery("SELECT * FROM users WHERE am = "+record.getOfficer_id());
               if(resultSet.next()){
                   officerName = resultSet.getString("last_name");
                   officerName+=" "+resultSet.getString("first_name");
               }
           }catch (SQLException e){
               logger.log(Level.SEVERE,"Error with the sql conn in the Print Report",e);
               new MessageBoxOk(e.getMessage());
           }




            XWPFParagraph bodyParagraph = document.createParagraph();
            XWPFRun bodyRun = bodyParagraph.createRun();
            bodyRun.setText("Σήμερα την ....."+ LocalDate.parse(record.getRecord_date()).getDayOfMonth()+"..... του μήνα ....."+month.getDisplayName(TextStyle.FULL,greekLocale)+"..... του έτους "+year+"....ημέρα "+day.getDisplayName(TextStyle.FULL,greekLocale));
            bodyRun.addBreak();
            bodyRun.setText("και ώρα "+ LocalTime.parse(record.getRecord_time()).withSecond(0).withNano(0)+" παρουσιάσθηκε ενώπιόν εμού του (2) "+officerName+" ........");
            bodyRun.addBreak();
            bodyRun.setText("ο (3) " + record.getFounder_last_name() + " " + record.getFounder_first_name()+" ......................................................................................");
            bodyRun.addBreak();
            bodyRun.setText(" του "+(record.getFounder_father_name()==null?"...........":record.getFounder_father_name())+" κάτοικος "+(record.getFounder_area_inhabitant()==null?"..........":record.getFounder_area_inhabitant())+" όδος "+(record.getFounder_street_address()==null?"..................................................................":record.getFounder_street_address()));
            bodyRun.addBreak();
            bodyRun.setText(" αριθμ. "+(record.getFounder_street_number()==null?"......":record.getFounder_street_number())+" κάτοχος του υπ'αριθμ.(4) "+record.getFounder_id_number()+"............................................");
            bodyRun.addBreak();
            bodyRun.setText("και        μου     παρέδωσε      το/α     κατωτέρω     περιγραφόμενα   αντικείμενα  τα");
            bodyRun.addBreak();
            bodyRun.setText("οποία και τα οποία ανευρέθησαν την "+LocalDate.parse(record.getFound_date().toString()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+" και ώρα "+LocalTime.parse(record.getRecord_time()).withNano(0).withSecond(0));
            bodyRun.addBreak();
            bodyRun.setText("στ(5) "+record.getFound_location()+".......................................................................................................");
            bodyRun.addBreak();
            bodyRun.setText("Περιγραφή: (6)  "+record.getItem_description());
            bodyRun.addBreak();

            for (int i = 0; i < 2; i++) {
                XWPFParagraph dotParagraph = document.createParagraph();
                XWPFRun dotRun = dotParagraph.createRun();
                dotRun.setText("....................................................................................................................................");
            }

            XWPFParagraph footerParagraph = document.createParagraph();
            XWPFRun footerRun = footerParagraph.createRun();
            footerRun.addBreak();
            footerRun.addBreak();
            footerRun.setFontSize(10);
            footerRun.setText("Ο ευρέτης, προσκομίζων την παρούσα, δικαιούται μετά ένα έτος, αν δεν ειδοποιηθεί ενωρίτερα,");
            footerRun.addBreak();
            footerRun.setText("να ζητήσει πληροφορίες για την τύχη τ.................... ανωτέρω αντικειμένου .................... από το (7) ....................");
            footerRun.addBreak();
            footerRun.setText("και να παραλάβει αυτό.................... αν δεν βρεθεί ο απωλέσας. Αν ο ευρέτης δεν");
            footerRun.addBreak();
            footerRun.setText("διαμένει στον ίδιο τόπο και δεν είναι δυνατή η ειδοποίησή του ή άφιξή του σε δέκα πέντε (15)");
            footerRun.addBreak();
            footerRun.setText("ημέρες, μετά τη λήξη του ενός έτους, το πράγμα παραδίδεται στον απωλέσαντα και ο ευρέτης");
            footerRun.addBreak();
            footerRun.setText("ασκεί τα νόμιμα δικαιώματά του απευθείας με τον κύριο και κάτοχο του πράγματος.");
            footerRun.addBreak();
            footerRun.setText("Για πιστοποίηση συντάσσεται η παρούσα και υπογράφεται:");
            footerRun.addBreak();
            footerRun.addBreak();

            // Signature lines
            XWPFParagraph signatureParagraph = document.createParagraph();
            signatureParagraph.setAlignment(ParagraphAlignment.BOTH);
            XWPFRun signatureRunLeft = signatureParagraph.createRun();
            signatureRunLeft.setText("Ο Ευρέτης και Παραδώσας");

            XWPFRun signatureRunRight = signatureParagraph.createRun();
            signatureRunRight.addTab();
            signatureRunRight.addTab();
            signatureRunRight.addTab();
            signatureRunRight.addTab();
            signatureRunRight.addTab();
            signatureRunRight.addTab();
            signatureRunRight.addTab();
            signatureRunRight.setText("Ο Παραλαβών");

            // Write the document to a file
            document.write(out);

            System.out.println("Document created successfully at " + filePathApodixi);
            new MessageBoxOk("Success Print the file");
        }catch (IOException e){
            logger.log(Level.SEVERE,"Cant print the file IO exception in printReport",e);
            e.printStackTrace();
            new MessageBoxOk(e.getMessage());
        }


    }


}
