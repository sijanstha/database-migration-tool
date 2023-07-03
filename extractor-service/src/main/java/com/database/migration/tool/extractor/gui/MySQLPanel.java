package com.database.migration.tool.extractor.gui;

import com.database.migration.tool.core.request.HandshakeRequest;
import com.database.migration.tool.core.request.HandshakeResponse;
import com.database.migration.tool.extractor.scripts.CMNDBConfig;
import com.database.migration.tool.extractor.scripts.Utils;
import com.database.migration.tool.extractor.config.MigratorServiceConfig;
import com.database.migration.tool.migrator.sdk.MigratorServiceApi;
import io.activej.inject.Injector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MySQLPanel extends JPanel implements ActionListener {

    private JButton btnCancel;
    private JButton btnNext;
    private JPanel rootPanel;
    private JLabel lblMysqlDatabaseConfiguration;
    private JLabel lblDbName;
    private JLabel lblIpAddress;
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JTextField txtDbName;
    private JLabel lblPort;
    private JLabel lblMysqlImg;
    private JTextField txtMysqlIp;
    private JTextField txtMysqlPort;
    private JTextField txtUname;
    private JPasswordField txtPwd;
    private MigratorServiceApi migratorServiceApi;

    public MySQLPanel(JPanel rootPanel) {
        this.rootPanel = rootPanel;
        setBackground(Color.WHITE);
        setBounds(0, 72, 550, 368);
        setLayout(null);

        btnNext = new JButton("Next");
        btnNext.addActionListener(this);
        btnNext.setBounds(290, 284, 89, 23);
        add(btnNext);

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBounds(418, 284, 89, 23);
        add(btnCancel);

        lblMysqlDatabaseConfiguration = new JLabel("MySQL Database Configuration ");
        lblMysqlDatabaseConfiguration.setBounds(170, 21, 338, 50);
        lblMysqlDatabaseConfiguration.setFont(new Font("Cambria Math", Font.PLAIN, 20));
        add(lblMysqlDatabaseConfiguration);

        lblIpAddress = new JLabel("IP Address:");
        lblIpAddress.setBounds(41, 82, 94, 20);
        add(lblIpAddress);

        txtMysqlIp = new JTextField();
        txtMysqlIp.setText("localhost");
        txtMysqlIp.setBounds(116, 82, 142, 20);
        add(txtMysqlIp);
        txtMysqlIp.setColumns(10);

        lblPort = new JLabel("Port:");
        lblPort.setBounds(297, 82, 46, 20);
        add(lblPort);

        txtMysqlPort = new JTextField();
        txtMysqlPort.setText("3306");
        txtMysqlPort.setBounds(335, 82, 80, 20);
        add(txtMysqlPort);
        txtMysqlPort.setColumns(10);

        lblDbName = new JLabel("DB Name:");
        lblDbName.setBounds(41, 116, 94, 20);
        add(lblDbName);

        txtDbName = new JTextField();
        txtDbName.setBounds(116, 116, 142, 20);
        add(txtDbName);
        txtDbName.setColumns(10);

        lblUsername = new JLabel("Username:");
        lblUsername.setBounds(41, 156, 94, 20);
        add(lblUsername);

        txtUname = new JTextField();
        txtUname.setBounds(116, 156, 142, 20);
        add(txtUname);
        txtUname.setColumns(10);

        lblPassword = new JLabel("Password:");
        lblPassword.setBounds(41, 194, 68, 20);
        add(lblPassword);

        txtPwd = new JPasswordField();
        txtPwd.setBounds(116, 194, 139, 20);
        add(txtPwd);

        lblMysqlImg = new JLabel();
        lblMysqlImg.setBounds(350, 90, 200, 200);
        lblMysqlImg.setIcon(new ImageIcon("res/mysql.png"));
        add(lblMysqlImg);

        Injector injector = Injector.of(new MigratorServiceConfig());
        this.migratorServiceApi = injector.getInstance(MigratorServiceApi.class);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == btnCancel) {
            System.exit(0);
        } else if (arg0.getSource() == btnNext) {
            String dbName = txtDbName.getText().trim();
            String dbIp = txtMysqlIp.getText().trim();
            String dbPort = txtMysqlPort.getText().trim();
            String dbUname = txtUname.getText().trim();
            String dbPwd = txtPwd.getText().trim();

            if (dbName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Error! Provide Destination Database Name");
                return;
            }
            if (!Utils.isAlphaNumeric(dbName)) {
                JOptionPane.showMessageDialog(this, "Error! Cannot accept invalid characters for DB Name");
                return;
            }
            if (dbIp.isEmpty()) {
                dbIp = "localhost";
            }
            if (dbPort.isEmpty()) {
                dbPort = "3306";
            }
            if (dbUname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Error! Provide MySQL UserName");
                return;
            }

            CMNDBConfig.setMYSQL_DB_NAME(dbName);
            CMNDBConfig.setMYSQL_IP(dbIp);
            CMNDBConfig.setMYSQL_PORT(dbPort);
            CMNDBConfig.setMYSQL_USER_NAME(dbUname);
            CMNDBConfig.setMYSQL_USER_PWD(dbPwd);

            HandshakeResponse handshakeResponse = this.migratorServiceApi.initiateInitialHandshake(buildHandshakeRequest());

            if (!handshakeResponse.isConnectionEstablished()) {
                JOptionPane.showMessageDialog(this, handshakeResponse.getMessage());
                return;
            }
            CMNDBConfig.setCONNECTION_ID(handshakeResponse.getConnectionId());

            JOptionPane.showMessageDialog(this, handshakeResponse.getMessage());
            rootPanel.remove(1);
            rootPanel.add(new IntermediatePanel(rootPanel), 1);
            rootPanel.repaint();
        }
    }

    private HandshakeRequest buildHandshakeRequest() {
        HandshakeRequest request = new HandshakeRequest();
        request.setMysqlhost(CMNDBConfig.getMYSQL_IP());
        request.setMysqluser(CMNDBConfig.getMYSQL_USER_NAME());
        request.setMysqlpassword(CMNDBConfig.getMYSQL_USER_PWD());
        request.setPort(Integer.parseInt(CMNDBConfig.getMYSQL_PORT()));
        request.setDbname(CMNDBConfig.getMYSQL_DB_NAME());
        return request;
    }
}
