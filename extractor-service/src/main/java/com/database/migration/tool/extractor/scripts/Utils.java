package com.database.migration.tool.extractor.scripts;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

    private Utils() {
    }

    public static boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9_]*$";
        return s.matches(pattern);
    }

    public boolean isValid(String s) {
        String pattern = "^[a-zA-Z0-9_ .,]*$";
        return s.matches(pattern);
    }

    public boolean isDateFormatCorrect(String s) {
        String pattern = "^[0-9 ./:-]*$";
        return s.matches(pattern);
    }

    //util to remove tblNames form error files
    public boolean removeFromErrorFile(String tmpTableName) {
        boolean status = false;
        File inputFile = new File("error_log.txt");
        File tempFile = new File("tmp_error_log.txt");

        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            writer = new BufferedWriter(new FileWriter(tempFile));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith(tmpTableName)) {
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
                reader.close();
                if (!inputFile.delete()) {
                    System.out.println("cannot delete error file");
                }
                status = tempFile.renameTo(inputFile);
                System.out.println("success");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status;
    }
}
