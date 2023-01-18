package com.database.migration.tool.extractor.service.dbtabledata;

import com.database.migration.tool.extractor.service.gui.LogWriterPanel;

import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Tool to run database scripts
 */
public class ScriptRunner {

    private static final String DEFAULT_DELIMITER = ";";
    private BufferedWriter fw;
    private Connection connection;
    private boolean autoCommit;

    private PrintWriter logWriter = new PrintWriter(System.out);
    private PrintWriter errorLogWriter = new PrintWriter(System.err);

    private String delimiter = DEFAULT_DELIMITER;
    private boolean fullLineDelimiter = false;

    private JPanel rootPanel;

    /**
     * Default constructor
     */
    public ScriptRunner(Connection connection, boolean autoCommit, JPanel rootPanel) {
        this.connection = connection;
        this.autoCommit = autoCommit;
        this.rootPanel = rootPanel;
    }

    public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
        this.delimiter = delimiter;
        this.fullLineDelimiter = fullLineDelimiter;
    }

    /**
     * Setter for logWriter property
     *
     * @param logWriter - the new value of the logWriter property
     */
    public void setLogWriter(PrintWriter logWriter) {
        this.logWriter = logWriter;
    }

    /**
     * Setter for errorLogWriter property
     *
     * @param errorLogWriter - the new value of the errorLogWriter property
     */
    public void setErrorLogWriter(PrintWriter errorLogWriter) {
        this.errorLogWriter = errorLogWriter;
    }

    /**
     * Runs an SQL script (read in using the Reader parameter)
     *
     * @param reader - the source of the script
     */
    public void runScript(Reader reader) throws IOException, SQLException {
        try {
            boolean originalAutoCommit = connection.getAutoCommit();
            try {
                if (originalAutoCommit != this.autoCommit) {
                    connection.setAutoCommit(this.autoCommit);
                }
                runScript(connection, reader);
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        } catch (IOException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error running script.  Cause: " + e, e);
        }
    }

    private void runScript(Connection conn, Reader reader) throws IOException, SQLException {
        LogWriterPanel dyna = new LogWriterPanel(rootPanel);
        rootPanel.remove(1);
        rootPanel.add(dyna, 1);
        rootPanel.repaint();
        //dyna.prepareAndShowGUI();

        File file = new File("error_log.txt");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fileout = new FileOutputStream(file);
        OutputStreamWriter outsw = new OutputStreamWriter(fileout, "UTF-8");
        fw = new BufferedWriter(outsw);
        StringBuffer command = null;
        try {
            LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--")) {
                    println(trimmedLine);
                } else if (trimmedLine.length() < 1 || trimmedLine.startsWith("//")) {
                    // Do nothing
                } else if (trimmedLine.length() < 1 || trimmedLine.startsWith("--")) {
                    // Do nothing
                } else if (!fullLineDelimiter && trimmedLine.endsWith(getDelimiter()) || fullLineDelimiter
                        && trimmedLine.equals(getDelimiter())) {
                    command.append(line.substring(0, line.lastIndexOf(getDelimiter())));
                    // command.append(" ");
                    Statement statement = conn.createStatement();

                    //println(command);
                    dyna.writeLog(command.toString());
                    try {
                        statement.execute(command.toString());
                    } catch (SQLException e) {
                        String errorSql = command.toString();
                        System.out.println(errorSql);
                        String stringTokens[] = errorSql.split("\t");
//                        System.out.println(stringTokens[2]);
//                        System.out.println(stringTokens[4]);
                        // printlnError(errorSql);
                        dyna.writeLog(errorSql);
                        // printlnError("Error Found And maintained log into error_log.txt");
                        fw.append(stringTokens[2] + "%");
                        fw.append(stringTokens[4]);
                        fw.append(System.getProperty("line.separator", "\n"));
                        //call a function to write error in log file
                        //printlnError(e.getMessage());
                    }

                    if (autoCommit && !conn.getAutoCommit()) {
                        conn.commit();
                    }

                    ResultSet rs = statement.getResultSet();
                    command = null;
                    try {
                        statement.close();
                    } catch (Exception e) {
                        // Ignore to workaround a bug in Jakarta DBCP
                    }
                    Thread.yield();
                } else {
                    command.append(line);
                    command.append(" ");
                }
            }
            if (!autoCommit) {
                conn.commit();
            }
        } catch (SQLException | IOException e) {
            printlnError("Error executing: " + command);
            printlnError(e);
            throw e;
        } finally {
            conn.rollback();
            flush();
        }
        fw.flush();
        fw.close();
        dyna.errorTableFrame();
    }

    private String getDelimiter() {
        return delimiter;
    }

    private void println(Object o) {
        if (logWriter != null) {
            logWriter.println(o);
        }
    }

    private void printlnError(Object o) {
        if (errorLogWriter != null) {
            errorLogWriter.println(o);
        }
    }

    private void flush() {
        if (logWriter != null) {
            logWriter.flush();
        }
        if (errorLogWriter != null) {
            errorLogWriter.flush();
        }
    }
}
