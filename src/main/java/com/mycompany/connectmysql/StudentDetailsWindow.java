package com.mycompany.connectmysql;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class StudentDetailsWindow extends JFrame {
    private int studentId;
    private String studentName;
    private String email;
    private int age;
    private String phoneNumber;
    private String billNumber;
    private String companyDatabase;
    private Connection con;
    private JComboBox<String> billNumberComboBox;
    private JTable paymentHistoryTable;
    private DefaultTableModel paymentTableModel;
    private JLabel courseNameLabel;
    private JLabel courseFeeLabel;
    private JLabel remainingFeeLabel;
    private JLabel totalPaidLabel;

    public StudentDetailsWindow(int studentId, String studentName, String companyDatabase) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.companyDatabase = companyDatabase;
        // connectToDatabase();
        // Connect to company database
        try {
            con = DatabaseUtil.connectToCompanyDatabase(companyDatabase);

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }

        setTitle("Student Details - " + studentName);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initializeUI();
        populateBillNumbers();
        fetchStudentDetails();
        fetchPaymentHistory();
    }

    // private void connectToDatabase() {
    // try {
    // Class.forName("com.mysql.cj.jdbc.Driver");
    // con = DriverManager.getConnection(
    // "jdbc:mysql://localhost:3306/" + companyDatabase +
    // "?zeroDateTimeBehavior=CONVERT_TO_NULL",
    // "springstudent", "springstudent");
    // } catch (Exception e) {
    // e.printStackTrace();
    // JOptionPane.showMessageDialog(null, "Failed to connect to database!",
    // "Error", JOptionPane.ERROR_MESSAGE);
    // }
    // }

    private void populateBillNumbers() {
        String query = "SELECT DISTINCT bill_number FROM payments WHERE user_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                billNumberComboBox.addItem(rs.getString("bill_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchStudentDetails() {
        String query = "SELECT email, age, contact FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                email = rs.getString("email");
                age = rs.getInt("age");
                phoneNumber = rs.getString("contact");

                JPanel detailsPanel = new JPanel();
                detailsPanel.add(new JLabel("Email: " + email));
                detailsPanel.add(new JLabel("Age: " + age));
                detailsPanel.add(new JLabel("Phone: " + phoneNumber));
                add(detailsPanel, BorderLayout.SOUTH);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void calculateAndDisplayRemainingFees() {
        if (billNumber == null) {
            remainingFeeLabel.setText("Remaining Fee: N/A");
            totalPaidLabel.setText("Total Paid: N/A");
            return;
        }

        String query = "SELECT SUM(amount_paid) AS total_paid " +
                "FROM payments " +
                "WHERE user_id = ? AND bill_number = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            pstmt.setString(2, billNumber);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double totalPaid = rs.getDouble("total_paid");
                String feeText = courseFeeLabel.getText();
                double courseFee = Double.parseDouble(feeText.replaceAll("[^0-9.]", ""));

                double remainingFee = courseFee - totalPaid;
                remainingFeeLabel.setText("Remaining Fee: " + remainingFee);
                totalPaidLabel.setText("Total Paid: " + totalPaid);
            } else {
                remainingFeeLabel.setText("Remaining Fee: N/A");
                totalPaidLabel.setText("Total Paid: N/A");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            remainingFeeLabel.setText("Remaining Fee: Error");
            totalPaidLabel.setText("Total Paid: Error");
        }
    }

    private void fetchCourseDetailsForSelectedBill() {
        billNumber = (String) billNumberComboBox.getSelectedItem();

        if (billNumber == null) {
            return;
        }

        String query = "SELECT c.name, c.fees " +
                "FROM payments p " +
                "JOIN enrollments e ON e.admission_id = p.admission_id " +
                "JOIN courses c ON c.id = e.course_id " +
                "WHERE p.bill_number = ? AND p.user_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, billNumber);
            pstmt.setInt(2, studentId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String courseName = rs.getString("name");
                int courseFee = rs.getInt("fees");

                courseNameLabel.setText("Course: " + courseName);
                courseFeeLabel.setText("Fee: " + courseFee);
            } else {
                courseNameLabel.setText("Course: N/A");
                courseFeeLabel.setText("Fee: N/A");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        calculateAndDisplayRemainingFees();
        fetchPaymentHistory();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Student Name: " + studentName));
        add(topPanel, BorderLayout.NORTH);

        billNumberComboBox = new JComboBox<>();
        billNumberComboBox.addActionListener(e -> {
            fetchCourseDetailsForSelectedBill();
            calculateAndDisplayRemainingFees();
        });
        topPanel.add(billNumberComboBox);

        JButton generatePDFButton = new JButton("Generate PDF");
        generatePDFButton.addActionListener(e -> generatePaymentHistoryPDF());
        topPanel.add(generatePDFButton);

        courseNameLabel = new JLabel("Course: ");
        courseFeeLabel = new JLabel("Fee: ");
        remainingFeeLabel = new JLabel("Remaining Fee: ");
        totalPaidLabel = new JLabel("Total Paid: ");

        topPanel.add(courseNameLabel);
        topPanel.add(courseFeeLabel);
        topPanel.add(remainingFeeLabel);
        topPanel.add(totalPaidLabel);

        paymentTableModel = new DefaultTableModel(new String[] { "Payment ID", "Amount Paid", "Payment Date" }, 0);
        paymentHistoryTable = new JTable(paymentTableModel);
        JScrollPane scrollPane = new JScrollPane(paymentHistoryTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void generatePaymentHistoryPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose where to save the PDF");
        fileChooser.setSelectedFile(
                new File("payment_history_" + billNumber + "_" + studentId + "_" + studentName + ".pdf"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
            document.open();

            Paragraph title = new Paragraph("Payment History",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.NORMAL, BaseColor.BLACK));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            document.add(
                    new Paragraph("Student Name: " + studentName, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            document.add(new Paragraph("Student ID: " + studentId));
            document.add(new Paragraph("Email: " + email));
            document.add(new Paragraph("Phone: " + phoneNumber));
            document.add(new Paragraph("Age: " + age));
            document.add(Chunk.NEWLINE);

            if (billNumber != null) {
                document.add(new Paragraph("Bill Number: " + billNumber));
                document.add(Chunk.NEWLINE);
            }

            document.add(new Paragraph(courseNameLabel.getText()));
            document.add(new Paragraph(courseFeeLabel.getText()));
            document.add(new Paragraph(remainingFeeLabel.getText()));
            document.add(new Paragraph(totalPaidLabel.getText()));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            PdfPCell cell = new PdfPCell(new Phrase("Payment ID"));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Amount Paid"));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Payment Date"));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            String query = "SELECT p.id, p.amount_paid, p.payment_date FROM payments p WHERE p.user_id = ? AND p.bill_number = ?";
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, studentId);
                pstmt.setString(2, billNumber);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    table.addCell(String.valueOf(rs.getInt("id")));
                    table.addCell(String.valueOf(rs.getDouble("amount_paid")));
                    table.addCell(new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("payment_date")));
                }
            }

            document.add(table);
            JOptionPane.showMessageDialog(this, "PDF saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save PDF.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            document.close();
        }
    }

    private void fetchPaymentHistory() {
        paymentTableModel.setRowCount(0);

        String query = "SELECT p.id, p.amount_paid, p.payment_date FROM payments p WHERE p.user_id = ? AND p.bill_number = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            pstmt.setString(2, billNumber);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getDouble("amount_paid"));
                row.add(rs.getDate("payment_date"));
                paymentTableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
