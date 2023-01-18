package com.database.migration.tool.extractor.service.gui;

import com.database.migration.tool.extractor.service.ImageResolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;

public class LogWriterPanel extends JPanel {

    private final JPanel rootPanel;
    private JTextArea tArea;
    private JScrollPane pane;
    private final ImageResolver resolver;

    public LogWriterPanel(JPanel rootPanel, ImageResolver resolver) {
        this.resolver = resolver;
        setBackground(Color.WHITE);
        setBounds(0, 72, 550, 368);
        setLayout(null);

        this.rootPanel = rootPanel;

        tArea = new JTextArea(10, 20);
        pane = new JScrollPane(tArea);
        pane.setBounds(5, 5, 530, 320);
        add(pane);
        tArea.setLineWrap(true);
        tArea.setWrapStyleWord(true);
        pane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }

        });
    }

    public void writeLog(String s) {
        tArea.append(s);
    }

    public void errorTableFrame() {
        File file = new File("error_log.txt");
        if (file.length() == 0) {
            rootPanel.remove(1);
            rootPanel.add(new ThankYouPanel(rootPanel), 1);
            rootPanel.repaint();
        } else {
            String[] options = new String[2];
            options[0] = new String("Proceed");
            options[1] = new String("Later");
            String message = "Some errors were found... Do you like to proceed to correct error in the tables?";
            int input = JOptionPane.showOptionDialog(this, message, "Confirm Dialog", 0, JOptionPane.INFORMATION_MESSAGE, null, options, null);
            if (input != 0) {
                //TODO SAVE USER PREFERENCE SO
                rootPanel.remove(1);
                rootPanel.add(new ThankYouPanel(rootPanel), 1);
                rootPanel.repaint();
            } else {
                System.out.println(input);
                rootPanel.remove(1);
                rootPanel.add(new ErrorTableListPanel(null, null), 1);
                rootPanel.repaint();
            }
        }
    }

}
