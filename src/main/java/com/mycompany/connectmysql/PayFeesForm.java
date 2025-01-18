/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.connectmysql;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.mysql.cj.x.protobuf.MysqlxCrud.Update;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 *
 * @author PULKIT SHARMA
 */
public class PayFeesForm extends javax.swing.JFrame {

    private JTextField amountField, billNumberField;
    private JLabel remainingFeesLabel, paidAmountLabel, studentIdLabel, studentNameLabel, emailLabel, courseNameLabel,
            totalFeesLabel;

    private Connection con;
    private int currentAmountPaid, remainingFees;
    private AdmissionsList parent;

    private String billNumber;
    private JComboBox<String> billNumberDropdown;
    private int admissionId;
    private int studentId;
    private String companyName;

    private JLabel totalInstallmentsLabel, paidInstallmentsLabel, nextInstallmentLabel;

    // Twilio credentials
    private static final String ACCOUNT_SID = ""; // Replace with your Twilio Account
                                                  // SID
    private static final String AUTH_TOKEN = ""; // Replace with your Twilio Auth Token
    private static final String TWILIO_PHONE_NUMBER = ""; // Replace with your Twilio
                                                          // phone
    // number

    public PayFeesForm(String companyName, int studentId, int admissionId, String billNumber, Connection con,
            AdmissionsList parent) {
        this.companyName = companyName;
        this.admissionId = admissionId;
        this.studentId = studentId;
        this.billNumber = billNumber;
        this.con = con;
        this.parent = parent;
        setTitle("Pay Fees");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeUI();
        fetchStudentData();
        fetchAllBillNumbers();
    }

    private void initializeUI() {
        setLayout(new GridLayout(15, 2, 5, 2));
        add(new JLabel("Select Bill Number:"));
        billNumberDropdown = new JComboBox<>();
        add(billNumberDropdown);

        add(new JLabel("Student ID:"));
        studentIdLabel = new JLabel();
        add(studentIdLabel);

        add(new JLabel("Student Name:"));
        studentNameLabel = new JLabel();
        add(studentNameLabel);

        add(new JLabel("Email:"));
        emailLabel = new JLabel();
        add(emailLabel);

        add(new JLabel("Course Name:"));
        courseNameLabel = new JLabel();
        add(courseNameLabel);

        add(new JLabel("Total Fees:"));
        totalFeesLabel = new JLabel();
        add(totalFeesLabel);

        add(new JLabel("Amount Paid:"));
        paidAmountLabel = new JLabel();
        add(paidAmountLabel);

        add(new JLabel("Remaining Fees:"));
        remainingFeesLabel = new JLabel();
        add(remainingFeesLabel);
        add(new JLabel("Total Installments:"));
        totalInstallmentsLabel = new JLabel();
        add(totalInstallmentsLabel);

        add(new JLabel("Paid Installments:"));
        paidInstallmentsLabel = new JLabel();
        add(paidInstallmentsLabel);

        add(new JLabel("Next Installment Amount:"));
        nextInstallmentLabel = new JLabel();
        add(nextInstallmentLabel);

        add(new JLabel("Enter Amount to Pay:"));
        amountField = new JTextField();
        add(amountField);

        // add(new JLabel("Enter Bill Number:"));
        // billNumberField = new JTextField();
        // add(billNumberField);
        // add(new JLabel("Select Bill Number:"));
        // billNumberDropdown = new JComboBox<>();
        // add(billNumberDropdown);

        JButton payButton = new JButton("Pay Fees");
        payButton.addActionListener(e -> payFees());
        add(payButton);

        JButton secondButton = new JButton("Print Receipt");
        secondButton.addActionListener(e -> printReceipt());

        add(secondButton);

        JButton shareButton = new JButton("Share Receipt");
        shareButton.addActionListener(e -> shareReceipt());
        add(shareButton);

        billNumberDropdown.addActionListener(e -> {
            System.out.println("Selected Bill: " + billNumberDropdown.getSelectedItem());
            handleBillNumberChange();
        });
        fetchInstallmentDetails();
    }

    private void fetchInstallmentDetails() {
        String query = "SELECT " +
                "(SELECT COUNT(*) FROM installments WHERE admission_id = ?) AS total_installments, " +
                "(SELECT COUNT(*) FROM installments WHERE admission_id = ? AND paid = 1) AS paid_installments, " +
                "(SELECT installment_amount FROM installments WHERE admission_id = ? AND paid = 0 ORDER BY installment_number ASC LIMIT 1) AS next_installment";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, admissionId); // Total installments
            pst.setInt(2, admissionId); // Paid installments
            pst.setInt(3, admissionId); // Next installment

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int totalInstallments = rs.getInt("total_installments");
                int paidInstallments = rs.getInt("paid_installments");
                double nextInstallment = rs.getDouble("next_installment");

                // Update labels
                totalInstallmentsLabel.setText(String.valueOf(totalInstallments));
                paidInstallmentsLabel.setText(String.valueOf(paidInstallments));

                if (rs.wasNull()) {
                    nextInstallmentLabel.setText("No pending installments");
                } else {
                    nextInstallmentLabel.setText("₹ " + nextInstallment);
                }
            } else {
                totalInstallmentsLabel.setText("0");
                paidInstallmentsLabel.setText("0");
                nextInstallmentLabel.setText("No pending installments");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching installment details: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBillNumberChange() {
        String selectedBillNumber = (String) billNumberDropdown.getSelectedItem();

        if (selectedBillNumber == null || selectedBillNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a valid bill number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Query to fetch the admission_id and other details based on the selected bill
        // number
        String query = "SELECT p.admission_id, u.name AS student_name, u.email, c.name AS course_name, c.fees, " +
                "a.amount_paid, (c.fees - a.amount_paid) AS remaining_fees " +
                "FROM payments p " +
                "JOIN admissions a ON p.admission_id = a.id " +
                "JOIN users u ON a.user_id = u.user_id " +
                "JOIN courses c ON a.course_id = c.id " +
                "WHERE p.bill_number = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, selectedBillNumber);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                admissionId = rs.getInt("admission_id");
                studentNameLabel.setText(rs.getString("student_name"));
                emailLabel.setText(rs.getString("email"));
                courseNameLabel.setText(rs.getString("course_name"));
                totalFeesLabel.setText("₹ " + rs.getString("fees"));
                paidAmountLabel.setText("₹ " + rs.getString("amount_paid"));

                int remainingFees = rs.getInt("remaining_fees");

                // Ensure remaining fee shows even negative values
                remainingFeesLabel.setText("₹ " + (remainingFees <= 0 ? remainingFees : Math.max(0, remainingFees)));
            } else {
                JOptionPane.showMessageDialog(this, "No data found for the selected bill number.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching bill details.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void fetchAllBillNumbers() {
        String query = "SELECT DISTINCT bill_number FROM payments WHERE user_id = ?"; // Use DISTINCT to avoid
                                                                                      // duplicates

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, studentId);
            ResultSet rs = pst.executeQuery();

            billNumberDropdown.removeAllItems(); // Clear previous items
            while (rs.next()) {
                billNumberDropdown.addItem(rs.getString("bill_number"));
            }

            // Ensure the dropdown reflects the selected bill number if already set
            if (billNumber != null && !billNumber.isEmpty()) {
                billNumberDropdown.setSelectedItem(billNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching bill numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchStudentData() {
        String admissionQuery = "SELECT a.amount_paid, c.fees, (c.fees - a.amount_paid) AS remaining_fees, " +
                "u.name, u.email, c.name AS course_name " +
                "FROM admissions a " +
                "JOIN users u ON a.user_id = u.user_id " +
                "JOIN courses c ON a.course_id = c.id " +
                "WHERE a.id = ?"; // Use admissionId here

        try (PreparedStatement pst = con.prepareStatement(admissionQuery)) {
            pst.setInt(1, admissionId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                studentIdLabel.setText(String.valueOf(studentId));
                studentNameLabel.setText(rs.getString("name"));
                emailLabel.setText(rs.getString("email"));
                courseNameLabel.setText(rs.getString("course_name"));
                totalFeesLabel.setText("₹ " + rs.getString("fees"));
                paidAmountLabel.setText("₹ " + rs.getString("amount_paid"));
                remainingFeesLabel.setText("₹ " + rs.getString("remaining_fees"));

                currentAmountPaid = rs.getInt("amount_paid");
                remainingFees = rs.getInt("remaining_fees");
            } else {
                JOptionPane.showMessageDialog(this, "No details found for the selected admission.", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching admission details.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshStudentData() {
        fetchStudentData(); // Re-use fetchStudentData to update labels
    }

    private void payFees() {
        try {
            // Validate the entered payment amount
            Double amountToPay = Double.parseDouble(amountField.getText());
            if (amountToPay <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid payment amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate the selected bill number
            String selectedBillNumber = (String) billNumberDropdown.getSelectedItem();
            if (selectedBillNumber == null || selectedBillNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a valid bill number.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Begin database transaction
            con.setAutoCommit(false);

            // Queries
            String insertPaymentQuery = "INSERT INTO payments (admission_id, user_id, bill_number, amount_paid, payment_date) "
                    +
                    "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
            String fetchInstallmentsQuery = "SELECT id, installment_amount, advance FROM installments " +
                    "WHERE admission_id = ? AND paid = 0 ORDER BY installment_number ASC";
            String updateInstallmentQuery = "UPDATE installments SET installment_amount = ?, paid = ?, payment_date = CURRENT_TIMESTAMP "
                    +
                    "WHERE id = ?";
            String updateAdmissionQuery = "UPDATE admissions SET amount_paid = amount_paid + ? WHERE id = ?";

            try (PreparedStatement insertPayment = con.prepareStatement(insertPaymentQuery);
                    PreparedStatement fetchInstallments = con.prepareStatement(fetchInstallmentsQuery);
                    PreparedStatement updateInstallment = con.prepareStatement(updateInstallmentQuery);
                    PreparedStatement updateAdmission = con.prepareStatement(updateAdmissionQuery)) {

                // Step 1: Insert the payment into the payments table
                insertPayment.setInt(1, admissionId);
                insertPayment.setInt(2, studentId);
                insertPayment.setString(3, selectedBillNumber);
                insertPayment.setDouble(4, amountToPay);
                insertPayment.executeUpdate();

                // Step 2: Fetch unpaid installments
                fetchInstallments.setInt(1, admissionId);
                ResultSet rs = fetchInstallments.executeQuery();

                // Step 3: Adjust installments
                while (rs.next() && amountToPay > 0) {
                    int installmentId = rs.getInt("id");
                    Double installmentAmount = rs.getDouble("installment_amount");

                    if (amountToPay >= installmentAmount) {
                        // Full payment for this installment
                        updateInstallment.setDouble(1, 0); // Remaining amount = 0
                        updateInstallment.setBoolean(2, true); // Mark as paid
                        updateInstallment.setInt(3, installmentId);
                        updateInstallment.executeUpdate();

                        amountToPay -= installmentAmount;
                    } else {
                        // Partial payment, adjust the remaining amount
                        Double remainingAmount = installmentAmount - amountToPay;
                        updateInstallment.setDouble(1, remainingAmount);
                        updateInstallment.setBoolean(2, false); // Not fully paid
                        updateInstallment.setInt(3, installmentId);
                        updateInstallment.executeUpdate();

                        amountToPay = 0.0; // No more amount left to pay
                    }
                }

                // Step 4: Update the admissions table
                updateAdmission.setDouble(1, Double.parseDouble(amountField.getText())); // Total amount paid
                updateAdmission.setInt(2, admissionId);
                updateAdmission.executeUpdate();

                // Commit the transaction
                con.commit();
                JOptionPane.showMessageDialog(this, "Payment recorded successfully!");

                // Refresh UI data
                fetchStudentData();
                fetchInstallmentDetails();

            } catch (SQLException e) {
                con.rollback();
                JOptionPane.showMessageDialog(this, "Error processing payment: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printReceipt() {
        String billNumber = billNumberDropdown.getSelectedItem().toString();
        // Creating a simple HTML template for the receipt
        String htmlContent = "<html><body style='font-family: Arial, sans-serif; margin: 20px;'>"
                + "<div style='text-align: center; border-bottom: 2px solid #000; padding-bottom: 10px;'>"
                + "<h1 style='margin: 0;'>" + companyName + "</h1>"
                + "<p style='margin: 5px 0; font-size: 18px;'>Payment Receipt</p>"
                + "</div>"
                + "<div style='margin-top: 20px;'>"
                + "<p><strong>Bill Number:</strong> " + billNumber + "</p>"
                + "<p><strong>Student ID:</strong> " + studentIdLabel.getText() + "</p>"
                + "<p><strong>Student Name:</strong> " + studentNameLabel.getText() + "</p>"
                + "<p><strong>Email:</strong> " + emailLabel.getText() + "</p>"
                + "<p><strong>Course Name:</strong> " + courseNameLabel.getText() + "</p>"
                + "<p><strong>Total Fees:</strong> " + totalFeesLabel.getText() + "</p>"
                + "<p><strong>Amount Paid:</strong> " + paidAmountLabel.getText() + "</p>"
                + "<p><strong>Remaining Fees:</strong> " + remainingFeesLabel.getText() + "</p>"
                + "<p><strong>Date:</strong> " + java.time.LocalDate.now() + "</p>"
                + "</div>"
                + "<div style='text-align: center; margin-top: 30px;'>"
                + "<p>Thank you for your payment!</p>"
                + "<p style='font-size: 14px;'>Please keep this receipt for your records.</p>"
                + "</div>"
                + "</body></html>";

        // Creating a JTextPane and setting the HTML content
        JTextPane jtp = new JTextPane();
        jtp.setContentType("text/html");
        jtp.setText(htmlContent);
        jtp.setEditable(false);

        // Displaying the receipt in a dialog before printing
        JOptionPane.showMessageDialog(this, new JScrollPane(jtp), "Receipt Preview", JOptionPane.INFORMATION_MESSAGE);

        // Attempting to print the receipt
        try {
            jtp.print();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error printing the receipt.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // private void shareReceipt() {
    // try {

    // File receiptFile = new File("Receipt_" + studentId + ".html");
    // try (FileWriter writer = new FileWriter(receiptFile)) {
    // writer.write(generateReceiptHTML());
    // }

    // String subject = "Payment Receipt";
    // String body = "Dear Student,%0A%0A"
    // + "Please find your payment receipt attached.%0A%0A"
    // + "Thank you,%0A"
    // + "BITS COMPUTER INSTITUTE";

    // String mailtoURI = String.format("mailto:?subject=%s&body=%s",
    // subject.replace(" ", "%20"),
    // body.replace(" ", "%20"));

    // Desktop.getDesktop().mail(new URI(mailtoURI));

    // JOptionPane.showMessageDialog(this, "Email client opened. Please attach the
    // receipt manually.", "Info",
    // JOptionPane.INFORMATION_MESSAGE);

    // } catch (IOException e) {
    // e.printStackTrace();
    // JOptionPane.showMessageDialog(this, "Error creating receipt file. Please try
    // again.", "Error",
    // JOptionPane.ERROR_MESSAGE);
    // } catch (Exception e) {
    // e.printStackTrace();
    // JOptionPane.showMessageDialog(this, "Error sharing the receipt. Please try
    // again.", "Error",
    // JOptionPane.ERROR_MESSAGE);
    // }
    // }

    private void shareReceipt() {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String messageContent = String.format(
                "Payment Receipt:\n" +
                        "\t\t" + companyName + "\n" +
                        "Bill Number: %s\n" +
                        "Student ID: %s\n" +
                        "Student Name: %s\n" +
                        "Course Name: %s\n" +
                        "Total Fees: %s\n" +
                        "Amount Paid: %s\n" +
                        "Remaining Fees: %s\n" +
                        "Date: %s\n" +
                        "Thank you for your payment!",
                billNumberDropdown.getSelectedItem(),
                studentIdLabel.getText(),
                studentNameLabel.getText(),
                courseNameLabel.getText(),
                totalFeesLabel.getText(),
                paidAmountLabel.getText(),
                remainingFeesLabel.getText(),
                java.time.LocalDate.now());

        // Recipient phone number
        String recipientPhoneNumber = "";

        try {
            // Send SMS
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(recipientPhoneNumber), // To
                    new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER), // From
                    messageContent // Message body
            ).create();

            // Success message
            JOptionPane.showMessageDialog(null,
                    "Receipt shared successfully! Message SID: " + message.getSid(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            // Error message
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error sharing the receipt: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * 4
     * 
     * 
     * 
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (int
         * 
         * 
         * or details see
         * http://download.oracle.com/java /tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PayFeesForm.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PayFeesForm.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PayFeesForm.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PayFeesForm.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            int studentId;
            int admissionId;
            Connection con;
            AdmissionsList parent;
            String billNumber;
            String companyName;

            public void run() {

                new PayFeesForm(companyName, studentId, admissionId, billNumber, con, parent).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
/////////////////
