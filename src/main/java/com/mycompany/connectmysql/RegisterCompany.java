package com.mycompany.connectmysql;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.io.File;
import java.io.IOException;

public class RegisterCompany extends javax.swing.JFrame {

    private JTextField companyNameField;
    private JButton registerButton;
    private JButton backButton;
    private JLabel statusLabel;

    public RegisterCompany() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JLabel companyNameLabel = new JLabel("Company Name:");
        companyNameField = new JTextField(20);
        registerButton = new JButton("Register Company");
        backButton = new JButton("Back");
        statusLabel = new JLabel();

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String companyName = companyNameField.getText().trim();
                if (!companyName.isEmpty()) {
                    registerCompany(companyName);
                } else {
                    statusLabel.setText("Company name cannot be empty.");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainPage window2 = new MainPage();
                window2.setVisible(true);
                dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.add(companyNameLabel);
        panel.add(companyNameField);
        panel.add(registerButton);
        panel.add(backButton);
        panel.add(statusLabel);

        add(panel);
        pack();
    }

    private void registerCompany(String companyName) {
        String centralDbUrl = "jdbc:mysql://localhost:3306/central_db";
        String centralDbUser = "springstudent";
        String centralDbPassword = "springstudent";

        try (Connection con = DriverManager.getConnection(centralDbUrl, centralDbUser, centralDbPassword)) {
            // Insert the company into the companies table
            String databaseName = companyName.replaceAll("\\s+", "_").toLowerCase();
            String insertQuery = "INSERT INTO companies (name, database_name) VALUES (?, ?)";
            try (PreparedStatement pst = con.prepareStatement(insertQuery)) {
                pst.setString(1, companyName);
                pst.setString(2, databaseName);
                pst.executeUpdate();
            }

            // Create the new database
            createNewDatabase(databaseName);

            statusLabel.setText("Company registered successfully with database: " + databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error registering company: " + e.getMessage());
        }
    }

    private void createNewDatabase(String databaseName) {
        String rootDbUrl = "jdbc:mysql://localhost:3306/";
        String rootDbUser = "springstudent";
        String rootDbPassword = "springstudent";

        try (Connection con = DriverManager.getConnection(rootDbUrl, rootDbUser, rootDbPassword)) {
            // Create the database
            String createDbQuery = "CREATE DATABASE " + databaseName;
            try (PreparedStatement pst = con.prepareStatement(createDbQuery)) {
                pst.executeUpdate();
            }

            // Copy the schema and data from the `pulkit` database
            copySchemaAndData("sample_project_db", databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating new database: " + e.getMessage());
        }
    }

    private void copySchemaAndData(String sourceDb, String targetDb) {
        try {
            String backupFile = sourceDb + "_backup.sql";

            // Ensure the backup file path is valid
            File backup = new File(backupFile);
            if (backup.exists())
                backup.delete();

            // Backup the source database
            String backupCommand = "mysqldump -u springstudent -pspringstudent " + sourceDb + " -r " + backupFile;
            executeCommand(backupCommand);

            // Restore the backup to the target database
            String restoreCommand = "mysql -u springstudent -pspringstudent " + targetDb + " < " + backupFile;
            executeCommand(restoreCommand);

            JOptionPane.showMessageDialog(null,
                    "Company Registered", "Insert MSG",
                    JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Database schema and data copied successfully from " + sourceDb + " to " + targetDb);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error copying database schema and data: " + e.getMessage());
        }
    }

    private void executeCommand(String command) throws IOException, InterruptedException {
        Process process;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            process = Runtime.getRuntime().exec(new String[] { "cmd.exe", "/c", command });
        } else {
            process = Runtime.getRuntime().exec(new String[] { "bash", "-c", command });
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Command failed with exit code: " + exitCode);
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new RegisterCompany().setVisible(true);
        });
    }
}
