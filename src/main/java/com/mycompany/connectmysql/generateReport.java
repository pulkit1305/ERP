package com.mycompany.connectmysql;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class generateReport extends javax.swing.JFrame {

    private String companyDatabase;
    private static String companyName;
    private Connection con;

    public generateReport(String companyName) {
        generateReport.companyName = companyName;
        setCompanyDatabase(companyName);
        initComponents();
        try {
            connectToDatabase();
            generateAndSaveReport();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "An error occurred while generating the report:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateAndSaveReport() throws Exception {
        String query = """
                SELECT DISTINCT
                    u.name AS student_name,
                    a.id AS admission_id,
                    a.user_id,
                    a.date_of_admission,
                    a.amount_paid AS admission_fee,
                    p.bill_number,
                    c.fees AS course_fee,
                    c.name AS course_name,
                    (c.fees - a.amount_paid) AS remaining_fee
                FROM admissions a
                JOIN users u ON a.user_id = u.user_id
                LEFT JOIN payments p ON a.id = p.admission_id
                LEFT JOIN courses c ON a.course_id = c.id;
                """;

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Admissions Report");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Student Name");
        headerRow.createCell(1).setCellValue("Admission ID");
        headerRow.createCell(2).setCellValue("User ID");
        headerRow.createCell(3).setCellValue("Bill Number");
        headerRow.createCell(4).setCellValue("Fee Paid");
        headerRow.createCell(5).setCellValue("Course Fee");
        headerRow.createCell(6).setCellValue("Remaining Fee");
        headerRow.createCell(7).setCellValue("Date of Admission");
        headerRow.createCell(8).setCellValue("Course Name");

        int rowNum = 1;
        while (rs.next()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rs.getString("student_name"));
            row.createCell(1).setCellValue(rs.getInt("admission_id"));
            row.createCell(2).setCellValue(rs.getInt("user_id"));
            row.createCell(3).setCellValue(rs.getString("bill_number"));
            row.createCell(4).setCellValue(rs.getDouble("admission_fee"));
            row.createCell(5).setCellValue(rs.getDouble("course_fee"));
            row.createCell(6).setCellValue(rs.getDouble("remaining_fee"));
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("date_of_admission"));
            row.createCell(7).setCellValue(formattedDate);
            row.createCell(8).setCellValue(rs.getString("course_name"));
        }

        LocalDate today = LocalDate.now();
        String folderName = companyName + " " + today.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                + " " + today.getYear();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Folder to Save Report");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            File companyMonthDir = new File(selectedDirectory, folderName);

            if (!companyMonthDir.exists()) {
                companyMonthDir.mkdir();
            }

            File reportFile = new File(companyMonthDir, "AdmissionsReport.xlsx");
            try (FileOutputStream fileOut = new FileOutputStream(reportFile)) {
                workbook.write(fileOut);
                JOptionPane.showMessageDialog(this,
                        "Report saved successfully in:\n" + reportFile.getAbsolutePath(),
                        "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "No folder selected. Report not saved.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        workbook.close();
        con.close();

        // Redirect to Admin Dashboard
        new AdminDashboard(companyName).setVisible(true);
        this.dispose();
    }

    private void setCompanyDatabase(String companyName) {
        connectToCentralDatabase();
        try {
            PreparedStatement pst = con.prepareStatement("SELECT database_name FROM companies WHERE name = ?");
            pst.setString(1, companyName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                companyDatabase = rs.getString("database_name");
            } else {
                JOptionPane.showMessageDialog(null, "Company not found in central database!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connectToCentralDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/central_db?zeroDateTimeBehavior=CONVERT_TO_NULL",
                    "springstudent", "springstudent");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to central database!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void connectToDatabase() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        if (companyDatabase != null) {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + companyDatabase + "?zeroDateTimeBehavior=CONVERT_TO_NULL",
                    "springstudent", "springstudent");
        } else {
            throw new Exception("Company database is not set. Please ensure company selection is done first.");
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); // Changed to DISPOSE_ON_CLOSE
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 300, Short.MAX_VALUE));
        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new generateReport(companyName).setVisible(true);
        });
    }
}
