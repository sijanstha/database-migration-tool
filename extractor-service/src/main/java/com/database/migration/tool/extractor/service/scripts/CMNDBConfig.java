
package com.database.migration.tool.extractor.service.scripts;

public class CMNDBConfig {
    private static String MYSQL_IP;
    private static String MYSQL_PORT;
    private static String MYSQL_USER_NAME;
    private static String MYSQL_USER_PWD;
    private static String MYSQL_DB_NAME;
    private static String MSACCESS_PATH;
    private static String MSACCESS_PWD;

    public static String getMYSQL_IP() {
        return MYSQL_IP;
    }

    public static void setMYSQL_IP(String MYSQL_IP) {
        CMNDBConfig.MYSQL_IP = MYSQL_IP;
    }

    public static String getMYSQL_PORT() {
        return MYSQL_PORT;
    }

    public static void setMYSQL_PORT(String MYSQL_PORT) {
        CMNDBConfig.MYSQL_PORT = MYSQL_PORT;
    }

    public static String getMYSQL_USER_NAME() {
        return MYSQL_USER_NAME;
    }

    public static void setMYSQL_USER_NAME(String MYSQL_USER_NAME) {
        CMNDBConfig.MYSQL_USER_NAME = MYSQL_USER_NAME;
    }

    public static String getMYSQL_USER_PWD() {
        return MYSQL_USER_PWD;
    }

    public static void setMYSQL_USER_PWD(String MYSQL_USER_PWD) {
        CMNDBConfig.MYSQL_USER_PWD = MYSQL_USER_PWD;
    }

    public static String getMYSQL_DB_NAME() {
        return MYSQL_DB_NAME;
    }

    public static void setMYSQL_DB_NAME(String MYSQL_DB_NAME) {
        CMNDBConfig.MYSQL_DB_NAME = MYSQL_DB_NAME;
    }

    public static String getMSACCESS_PATH() {
        return MSACCESS_PATH;
    }

    public static void setMSACCESS_PATH(String MSACCESS_PATH) {
        CMNDBConfig.MSACCESS_PATH = MSACCESS_PATH;
    }

    public static String getMSACCESS_PWD() {
        return MSACCESS_PWD;
    }

    public static void setMSACCESS_PWD(String MSACCESS_PWD) {
        CMNDBConfig.MSACCESS_PWD = MSACCESS_PWD;
    }
}
