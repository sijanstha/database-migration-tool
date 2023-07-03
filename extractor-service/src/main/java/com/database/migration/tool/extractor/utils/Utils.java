package com.database.migration.tool.extractor.utils;

import java.net.URL;

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

    public static String loadStaticFile(String fileName) {
        String qualifiedFileName = String.format("static/%s", fileName);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL classLoaderResource = classLoader.getResource(qualifiedFileName);
        if (classLoaderResource == null)
            return null;

        return classLoaderResource.getPath();
    }
}
