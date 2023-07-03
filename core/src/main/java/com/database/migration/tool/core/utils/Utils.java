package com.database.migration.tool.core.utils;

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

    public static String loadStaticImageFile(String fileName) {
        return loadResources(String.format("static/%s", fileName));
    }

    public static String loadResources(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL classLoaderResource = classLoader.getResource(fileName);
        if (classLoaderResource == null)
            return null;

        return classLoaderResource.getPath();
    }
}