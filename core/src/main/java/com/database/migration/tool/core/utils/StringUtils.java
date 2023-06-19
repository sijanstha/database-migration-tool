package com.database.migration.tool.core.utils;

import java.util.UUID;

public class StringUtils {

    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String generateUniqueConnectionId() {
        return UUID.randomUUID().toString();
    }
}
