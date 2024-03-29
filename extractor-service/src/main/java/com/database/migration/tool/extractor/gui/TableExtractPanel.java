package com.database.migration.tool.extractor.gui;

import com.database.migration.tool.extractor.service.TableDataExtractor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TableExtractPanel extends JPanel {

    private JPanel rootPanel;
    private JTextArea txtlog;
    private ArrayList<String> selectedTables;

    public TableExtractPanel(JPanel rootPanel, ArrayList<String> selectedTables) {
        this.rootPanel = rootPanel;
        this.selectedTables = selectedTables;
        setBackground(Color.WHITE);
        setBounds(0, 72, 550, 368);
        setLayout(null);

        txtlog = new JTextArea();
        txtlog.setEditable(false);
        txtlog.setBounds(21, 11, 486, 234);
        add(txtlog);

        process();
    }

    public void process() {

        txtlog.setFont(new Font("Cambria Math", Font.PLAIN, 16));
        txtlog.append("Please Wait\r\n\n");
        txtlog.append("Generating SQL Queries\r\n");
        txtlog.append("This may take a while...Keep Patience\r\n\n");
        txtlog.append("Feel free to take a cup of tea..we will automatically execute after \n"
                + "generating all queries :) \n");
        TableDataExtractor tblExtract = new TableDataExtractor(selectedTables, txtlog);
        try {
            List<Future<?>> futures = tblExtract.databaseMigrator();
            for(Future<?> future : futures)
                future.get();

//            rootPanel.remove(1);
//            rootPanel.add(new ThankYouPanel(rootPanel), 1);
//            rootPanel.repaint();
            txtlog.append("\nYour request for db migration has been completed!");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
