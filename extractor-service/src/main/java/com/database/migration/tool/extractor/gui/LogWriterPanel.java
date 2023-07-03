package com.database.migration.tool.extractor.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;

public class LogWriterPanel extends JPanel {
    private JTextArea tArea;
    private JScrollPane pane;

    public LogWriterPanel() {
        setBackground(Color.WHITE);
        setBounds(0, 72, 550, 368);
        setLayout(null);

        tArea = new JTextArea(10, 20);
        pane = new JScrollPane(tArea);
        pane.setBounds(5, 5, 530, 320);
        add(pane);
        tArea.setLineWrap(true);
        tArea.setWrapStyleWord(true);
        pane.getVerticalScrollBar().addAdjustmentListener(e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum()));
    }

    public void writeLog(String s) {
        tArea.append(s);
    }

}
