package com.database.migration.tool.extractor.service;

import com.database.migration.tool.extractor.service.gui.ErrorTableListPanel;
import com.database.migration.tool.extractor.service.gui.JHeaderPanel;
import com.database.migration.tool.extractor.service.gui.WelcomePanel;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;

import javax.swing.*;

public class ConfigModule extends AbstractModule {
    @Provides
    public ImageResolver imageResolver() {
        return new ImageResolver();
    }

    @Provides
    public JPanel root() {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        JHeaderPanel headerPanel = new JHeaderPanel();
        headerPanel.setBounds(0, 0, 550, 72);
        contentPane.add(headerPanel);
        return contentPane;
    }

    @Provides
    public WelcomePanel welcomePanel(JPanel root, ImageResolver resolver) {
        return new WelcomePanel(root, resolver);
    }

    @Provides
    public ErrorTableListPanel errorTableListPanel(JPanel root, ImageResolver resolver) {
        return new ErrorTableListPanel(root, resolver);
    }
}
