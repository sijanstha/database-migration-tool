package com.database.migration.tool.extractor.service.gui;

import com.database.migration.tool.extractor.service.dbtabledata.TableColumnMetadata;
import com.database.migration.tool.extractor.service.scripts.AppMessage;
import com.database.migration.tool.extractor.service.scripts.CMNDBConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class IntermediatePanel extends JPanel implements ActionListener {

    private JButton btnCancel;
    private JButton btnNext;
    private JPanel rootPanel;
    private AppMessage appMsg;
    private JTextArea txtlog;

    public IntermediatePanel(JPanel rootPanel) {
        this.rootPanel = rootPanel;
        setBackground(Color.WHITE);
        setBounds(0, 72, 550, 368);
        setLayout(null);

        btnNext = new JButton("Next");
        btnNext.setBounds(290, 284, 89, 23);
        add(btnNext);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(418, 284, 89, 23);
        add(btnCancel);

        txtlog = new JTextArea();
        txtlog.setEditable(false);
        txtlog.setBounds(21, 11, 486, 234);
        add(txtlog);

        btnNext.addActionListener(this);
        btnCancel.addActionListener(this);

        process();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == btnNext) {
            rootPanel.remove(1);
            rootPanel.add(new SelectTablePanel(rootPanel), 1);
            rootPanel.repaint();
        } else if (arg0.getSource() == btnCancel) {
            int input = JOptionPane.showConfirmDialog(this, "Are you sure want to abort ongoing process?", "Confirm Dialog", JOptionPane.OK_CANCEL_OPTION);
            if (input == 0) {
                File file = new File("table_meta_data.txt");
                if (file.exists()) {
                    file.delete();
                }
                System.exit(0);
            }
        }
    }

    public void process() {

        txtlog.append("Please Wait.....\r\n\n");
        txtlog.append(saveProperties());
        txtlog.append("Fetching Database Metadata.....\r\n\n");
        appMsg = new TableColumnMetadata().getTableMetadata();
        if (appMsg.getCODE() != 0) {
            txtlog.append(appMsg.getMSG());
        } else {
            txtlog.append(appMsg.getMSG());
        }
    }

    public String saveProperties() {
        String msg = "";
        String userHome = System.getProperty("user.home");
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream(userHome+"/config.properties");
            prop.setProperty("mysqlIp", CMNDBConfig.getMYSQL_IP());
            prop.setProperty("mysqlPort", CMNDBConfig.getMYSQL_PORT());
            prop.setProperty("mysqlDbName", CMNDBConfig.getMYSQL_DB_NAME());
            prop.setProperty("mysqlDbUser", CMNDBConfig.getMYSQL_USER_NAME());
            prop.setProperty("mysqlDbPwd", CMNDBConfig.getMYSQL_USER_PWD());
            prop.setProperty("msaccessPath", CMNDBConfig.getMSACCESS_PATH());
            prop.setProperty("msaccessPwd", CMNDBConfig.getMSACCESS_PWD());
            prop.store(output, null);
            msg = "Application Properties Saved\n\n";
        } catch (IOException io) {
            msg = io.getMessage();
            System.out.println(io.toString());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    msg = e.getMessage();
                    System.out.println(e.toString());
                }
            }
        }
        return msg;
    }
}
