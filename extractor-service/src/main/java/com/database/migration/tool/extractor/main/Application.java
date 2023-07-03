package com.database.migration.tool.extractor.main;

import com.database.migration.tool.core.utils.Utils;
import com.database.migration.tool.extractor.gui.JRootFrame;

import javax.swing.*;
import java.awt.*;

public class Application {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JRootFrame frame = new JRootFrame();
            frame.setResizable(false);
            frame.setTitle("Database Migration Wizard v1.0");
            String file = Utils.loadStaticImageFile("logo.jpg");
            System.out.print(file);
            ImageIcon icon = new ImageIcon(file);
            frame.setIconImage(icon.getImage());
            frame.setVisible(true);
        });
    }
}
