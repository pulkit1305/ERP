/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
*/
package com.mycompany.connectmysql;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

//import javax.swing.JTextPane;

/**
 *
 * @author PULKIT SHARMA
 */
public final class AdmissionForm extends javax.swing.JFrame {

    private JTextField ageField, contactField, emailField, amountPaidField, feeField, installmentMonthsField;
    private JComboBox<String> courseDropdown, startMonthDropdown;
    private Connection con;
    private JComboBox<String> nameDropdown;
    private String companyName;
    private String companyDatabase;
    private JLabel imageLabel;

    public AdmissionForm(String companyName) {
        this.companyName = companyName;

        setCompanyDatabase(companyName);
        if (companyDatabase == null) {
            JOptionPane.showMessageDialog(null, "Failed to determine the company database. Please contact support.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose(); // Close the form if the company database is not set
            return;
        }

        // Set up the main frame
        setTitle("Student Admission Form - " + companyName);
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Create a panel for the top section (Image + Back button + Heading)
        JPanel topPanel = new JPanel(new BorderLayout());

        // Add Image
        imageLabel = new JLabel("", JLabel.CENTER);
        loadImageIntoLabel("/images/1089.jpg", imageLabel, 100, 50); // Adjust path if needed
        topPanel.add(imageLabel, BorderLayout.NORTH);

        // Add Back button
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.addActionListener(e -> {
            AdminDashboard window2 = new AdminDashboard(companyName);
            window2.setVisible(true);
            dispose();
        });

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.add(backButton);

        // Add Heading
        JLabel headingLabel = new JLabel(companyName, JLabel.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Combine Back button and heading into the top panel
        topPanel.add(backButtonPanel, BorderLayout.WEST);
        topPanel.add(headingLabel, BorderLayout.CENTER);

        // Add the top panel to the frame
        add(topPanel, BorderLayout.NORTH);

        // Create a panel for the form fields
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10)); // Increased row count to accommodate new fields

        // Add form components
        formPanel.add(new JLabel("Name:"));
        nameDropdown = new JComboBox<>();
        nameDropdown.setEditable(true);
        nameDropdown.setMaximumRowCount(5);
        formPanel.add(nameDropdown);

        formPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        formPanel.add(ageField);

        formPanel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        formPanel.add(contactField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Course:"));
        courseDropdown = new JComboBox<>(getCourses());
        courseDropdown.addActionListener(new CourseSelectionAction());
        formPanel.add(courseDropdown);

        formPanel.add(new JLabel("Total Fees:"));
        feeField = new JTextField("₹ 8000");
        feeField.setEditable(false);
        formPanel.add(feeField);

        formPanel.add(new JLabel("Amount Paid (Initial):"));
        amountPaidField = new JTextField("0");
        formPanel.add(amountPaidField);

        formPanel.add(new JLabel("Number of Installments:"));
        installmentMonthsField = new JTextField("0");
        formPanel.add(installmentMonthsField);

        // Add label for Start Month for Installment
        // Add label for Start Month for Installment
        formPanel.add(new JLabel("Start Month for Installment:"));

        // Get current month and year
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH); // 0 for January, 11 for December
        int currentYear = calendar.get(Calendar.YEAR);

        // Create array to store month-year combinations (full month name and year)
        String[] monthsWithYears = new String[12];
        int monthCounter = 0;
        for (int i = 1; i <= 12; i++) {
            int monthIndex = (currentMonth + i) % 12;
            int year = currentYear + ((currentMonth + i) / 12);
            monthsWithYears[monthCounter] = getMonthName(monthIndex) + " " + year;
            monthCounter++;
        }

        // Create JComboBox with future months and years
        startMonthDropdown = new JComboBox<>(monthsWithYears);
        formPanel.add(startMonthDropdown);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitAction());
        formPanel.add(submitButton);

        // Add the form panel to the center
        add(formPanel, BorderLayout.CENTER);

        setVisible(true);

        // Add listeners for dynamic name suggestions
        addNameSearchListeners();
        addNameSelectionListener();
    }

    public static String getMonthName(int monthIndex) {
        String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };
        return months[monthIndex];
    }

    private void loadImageIntoLabel(String imagePath, JLabel label, int width, int height) {
        try {
            // Load the image using the class loader
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(imagePath));
            Image resizedImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            // Set the resized icon to the JLabel
            label.setIcon(resizedIcon);
            label.setText(""); // Clear any default text
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading image: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setCompanyDatabase(String companyName) {
        connectToCentralDatabase(); // Connect to the central_db first
        try {
            PreparedStatement pst = con.prepareStatement("SELECT database_name FROM companies WHERE name = ?");
            pst.setString(1, companyName);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                companyDatabase = rs.getString("database_name"); // Get the database name for the company
            } else {
                JOptionPane.showMessageDialog(null, "Company not found in central database!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Central database connection method for fetching company database
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

    // Modify admission process to set the company database first

    private void performAdmission(String companyName, String name, int age, String contact, String email, String course,
            Double amountPaid, int installmentMonths, String startMonth) {
        setCompanyDatabase(companyName);
        connectToDatabase();
        saveAdmission(name, age, contact, email, course, amountPaid, installmentMonths, startMonth);

    }

    private void addNameSearchListeners() {
        nameDropdown.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = nameDropdown.getEditor().getItem().toString(); // Get text from combo box editor
                if (input.length() >= 1) {
                    showSuggestions(input); // Dynamically show suggestions
                } else {
                    nameDropdown.hidePopup(); // Hide dropdown if input is empty
                }
            }
        });
    }

    private void addNameSelectionListener() {
        nameDropdown.addActionListener(e -> {
            if (e.getActionCommand().equals("comboBoxChanged")) {
                // Only populate fields when a suggestion is selected
                String selectedName = (String) nameDropdown.getSelectedItem();
                if (selectedName != null && !selectedName.isEmpty()) {
                    fetchAndFillDetails(selectedName); // Fetch details and update fields
                }
            }
        });
    }

    private void showSuggestions(String input) {
        connectToDatabase();
        try {
            // Prepare SQL query to find names matching the input
            PreparedStatement pst = con.prepareStatement("SELECT name FROM users WHERE name LIKE ?");
            pst.setString(1, "%" + input + "%"); // Match names that contain the input text
            ResultSet rs = pst.executeQuery();

            // Clear existing suggestions
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) nameDropdown.getModel();
            model.removeAllElements();
            model.addElement(input); // Keep user-typed input as the first entry

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                String suggestion = rs.getString("name");

                // Add suggestion to the combo box model
                model.addElement(suggestion);
            }

            // Show the dropdown if there are suggestions, otherwise hide it
            if (hasResults) {
                nameDropdown.showPopup(); // Show the dropdown
            } else {
                nameDropdown.hidePopup(); // Hide the dropdown if no suggestions
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchAndFillDetails(String name) {
        connectToDatabase();
        try {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE name = ?");
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Fill the fields with the existing student's details
                ageField.setText(String.valueOf(rs.getInt("age")));
                contactField.setText(rs.getString("contact"));
                emailField.setText(rs.getString("email"));

                // Make the fields non-editable
                ageField.setEditable(false);
                contactField.setEditable(false);
                emailField.setEditable(false);
            } else {
                // Enable the fields for new students
                ageField.setEditable(true);
                contactField.setEditable(true);
                emailField.setEditable(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class CourseSelectionAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedCourse = (String) courseDropdown.getSelectedItem();
            try {
                PreparedStatement pst = con.prepareStatement("SELECT fees FROM courses WHERE name = ?");
                pst.setString(1, selectedCourse);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    feeField.setText("₹ " + String.valueOf(rs.getInt("fees")));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Fetch courses from database
    private String[] getCourses() {
        connectToDatabase();
        try {
            PreparedStatement pst = con.prepareStatement("SELECT name FROM courses");
            ResultSet rs = pst.executeQuery();
            ArrayList<String> courses = new ArrayList<>();
            while (rs.next()) {
                courses.add(rs.getString("name"));
            }
            return courses.toArray(new String[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[] { "No Courses Available" };
        }
    }

    // Database connection
    public void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            if (companyDatabase != null) {
                // Use the dynamically determined database
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/" + companyDatabase + "?zeroDateTimeBehavior=CONVERT_TO_NULL",
                        "springstudent", "springstudent");
            } else {
                throw new Exception("Company database is not set. Please ensure company selection is done first.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to company database!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private class SubmitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = (String) nameDropdown.getEditor().getItem();
            int age = Integer.parseInt(ageField.getText());
            String contact = contactField.getText();
            String email = emailField.getText();
            String course = (String) courseDropdown.getSelectedItem();
            Double amountPaid = Double.parseDouble(amountPaidField.getText());
            int installmentMonths = Integer.parseInt(installmentMonthsField.getText());
            String startMonth = (String) startMonthDropdown.getSelectedItem();
            int userId = getUserId(name, contact); // Check if user exists

            if (userId == -1) {
                // New user, save admission
                performAdmission(companyName, name, age, contact, email, course, amountPaid, installmentMonths,
                        startMonth);
            } else {
                // Existing user, check if they're already enrolled in the course
                if (isAlreadyEnrolled(userId, course)) {
                    // Show popup if already enrolled
                    JOptionPane.showMessageDialog(null, "User is already enrolled in this course.");
                } else {
                    // Enroll in a new course and generate bill number
                    performAdmission(companyName, name, age, contact, email, course, amountPaid, installmentMonths,
                            startMonth);
                }
            }
        }
    }

    private boolean isAlreadyEnrolled(int userId, String course) {
        try {
            PreparedStatement pst = con
                    .prepareStatement(
                            "SELECT * FROM enrollments WHERE user_id = ? AND course_id = (SELECT id FROM courses WHERE name = ?)");
            pst.setInt(1, userId);
            pst.setString(2, course);
            ResultSet rs = pst.executeQuery();
            return rs.next(); // If a record is found, it means the user is already enrolled
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getUserId(String name, String contact) {
        try {
            PreparedStatement pst = con.prepareStatement("SELECT user_id FROM users WHERE name = ? AND contact = ?");
            pst.setString(1, name);
            pst.setString(2, contact);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // User not found
    }

    // Save Admission and Payment Details
    private void saveAdmission(String name, int age, String contact, String email, String courseName, Double amountPaid,
            int installmentMonths, String startMonth) {
        connectToDatabase();
        try {
            // Step 1: Insert user into users table if they don't exist
            int userId = getUserId(name, contact);
            if (userId == -1) {
                // If user doesn't exist, insert them
                String userQuery = "INSERT INTO users (name, age, contact, email) VALUES (?, ?, ?, ?)";
                PreparedStatement userPst = con.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
                userPst.setString(1, name);
                userPst.setInt(2, age);
                userPst.setString(3, contact);
                userPst.setString(4, email);
                userPst.executeUpdate();

                ResultSet rs = userPst.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1); // Get the generated user ID
                }
            }

            // Step 2: Get course details
            PreparedStatement coursePst = con.prepareStatement("SELECT id, fees FROM courses WHERE name = ?");
            coursePst.setString(1, courseName);
            ResultSet courseRs = coursePst.executeQuery();
            int courseId = 0;
            Double totalFees = 0.0;
            if (courseRs.next()) {
                courseId = courseRs.getInt("id");
                totalFees = courseRs.getDouble("fees");
            }
            Double remainingAmount = totalFees - amountPaid;
            // Step 3: Insert admission details with unique reference number
            String referenceNumber = "REF" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                    + new Random().nextInt(9000); // Generate admission reference number
            String admissionQuery = "INSERT INTO admissions (user_id, course_id, date_of_admission, amount_paid, reference_number) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement admissionPst = con.prepareStatement(admissionQuery, Statement.RETURN_GENERATED_KEYS);
            admissionPst.setInt(1, userId);
            admissionPst.setInt(2, courseId);
            admissionPst.setDate(3, Date.valueOf(LocalDate.now()));
            admissionPst.setDouble(4, amountPaid);
            admissionPst.setString(5, referenceNumber);
            admissionPst.executeUpdate();

            ResultSet rs = admissionPst.getGeneratedKeys();
            int admissionId = 0;
            if (rs.next()) {
                admissionId = rs.getInt(1); // Get generated admission_id
            }

            // Step 4: Generate new bill number for the payment
            String billNumber = "BIL" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                    + new Random().nextInt(9000); // Generate bill number
            String paymentQuery = "INSERT INTO payments (admission_id, user_id, amount_paid, payment_date, bill_number) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement paymentPst = con.prepareStatement(paymentQuery);
            paymentPst.setInt(1, admissionId);
            paymentPst.setInt(2, userId);
            paymentPst.setDouble(3, amountPaid);
            paymentPst.setDate(4, Date.valueOf(LocalDate.now()));
            paymentPst.setString(5, billNumber);
            paymentPst.executeUpdate();

            // Step 5: Insert enrollment details into enrollments table
            String enrollmentQuery = "INSERT INTO enrollments (user_id, course_id, enrollment_date, admission_id) VALUES (?, ?, ?, ?)";
            PreparedStatement enrollmentPst = con.prepareStatement(enrollmentQuery);
            enrollmentPst.setInt(1, userId);
            enrollmentPst.setInt(2, courseId);
            enrollmentPst.setDate(3, Date.valueOf(LocalDate.now()));
            enrollmentPst.setInt(4, admissionId);
            enrollmentPst.executeUpdate();

            // Installment Calculation
            if (installmentMonths > 0 && remainingAmount > 0) {
                generateInstallments(admissionId, userId, courseId, remainingAmount, installmentMonths, startMonth);
            }

            // Step 6: Generate and print slips
            generateAndPrintSlips(name, email, courseName, totalFees, amountPaid, referenceNumber, billNumber);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateInstallments(int admissionId, int userId, int courseId, Double remainingAmount, int months,
            String startMonth) throws SQLException {
        Double installmentAmount = remainingAmount / months;

        // Parse the selected start month and year
        String[] startMonthParts = startMonth.split(" ");
        String monthName = startMonthParts[0];
        int startYear = Integer.parseInt(startMonthParts[1]);

        // Map month name to numeric month value (1-12)
        Month startMonthEnum = Month.valueOf(monthName.toUpperCase());
        int startMonthIndex = startMonthEnum.getValue();

        for (int i = 0; i < months; i++) {
            // Calculate the due month and year
            int dueMonth = (startMonthIndex + i - 1) % 12 + 1; // Wrap around using modulo
            int dueYear = startYear + (startMonthIndex + i - 1) / 12; // Increment year when crossing December

            // Create due date for the installment
            String dueDate = LocalDate.of(dueYear, dueMonth, 1).toString();

            // Insert installment into the database
            PreparedStatement installmentPst = con.prepareStatement(
                    "INSERT INTO installments (admission_id, user_id, course_id, installment_number, installment_amount, due_date, paid) VALUES (?, ?, ?, ?, ?, ?, ?)");
            installmentPst.setInt(1, admissionId);
            installmentPst.setInt(2, userId);
            installmentPst.setInt(3, courseId);
            installmentPst.setInt(4, i + 1);
            installmentPst.setDouble(5, installmentAmount);
            installmentPst.setString(6, dueDate);
            installmentPst.setInt(7, 0); // Set as unpaid initially
            installmentPst.executeUpdate();
        }
    }

    // Handle Advance Installment Payment
    private void payAdvanceInstallments(int admissionId, int monthsToPay) throws SQLException {
        PreparedStatement fetchPst = con.prepareStatement(
                "SELECT * FROM installments WHERE admission_id = ? AND paid = 0 ORDER BY due_date LIMIT ?");
        fetchPst.setInt(1, admissionId);
        fetchPst.setInt(2, monthsToPay);
        ResultSet rs = fetchPst.executeQuery();

        while (rs.next()) {
            int installmentId = rs.getInt("id");
            PreparedStatement updatePst = con.prepareStatement(
                    "UPDATE installments SET paid = 1, payment_date = ? WHERE id = ?");
            updatePst.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            updatePst.setInt(2, installmentId);
            updatePst.executeUpdate();
        }
    }

    // Generate and print admission and payment slips
    private void generateAndPrintSlips(String name, String email, String courseName, Double totalFees,
            Double amountPaid,
            String refNumber, String billNumber) {
        Double remainingFees = totalFees - amountPaid;
        String admissionSlip = generateHTMLSlip(name, email, courseName, totalFees, amountPaid, remainingFees,
                refNumber, false);
        String paymentSlip = generateHTMLSlip(name, email, courseName, totalFees, amountPaid, remainingFees, billNumber,
                true);
        // Display a preview before printing
        showPreviewDialog("Admission Slip Preview", admissionSlip);
        showPreviewDialog("Payment Slip Preview", paymentSlip);

        JTextPane jtp = new JTextPane();
        jtp.setContentType("text/html");

        // Print Admission Slip
        jtp.setText(admissionSlip);
        try {
            jtp.print();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Print Payment Slip
        jtp.setText(paymentSlip);
        try {
            jtp.print();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Admission and Payment Slips Printed Successfully!");
    }

    private void showPreviewDialog(String title, String htmlContent) {
        JTextPane previewPane = new JTextPane();
        previewPane.setContentType("text/html");
        previewPane.setText(htmlContent);
        previewPane.setEditable(false); // Disable editing

        JScrollPane scrollPane = new JScrollPane(previewPane);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);
    }

    private String generateHTMLSlip(String name, String email, String courseName, Double totalFees, Double amountPaid,
            Double remainingFees, String number, boolean isPaymentSlip) {
        String header = isPaymentSlip ? "<h2>Payment Slip</h2><p><b>Bill Number:</b> " + number + "</p>"
                : "<h2>Admission Confirmation</h2><p><b>Reference Number:</b> " + number + "</p>";

        return "<html><head><style>" +
                "body { font-family: Arial, sans-serif; background-color: #eef2f7; }" +
                ".slip { max-width: 400px; margin: 40px auto; padding: 20px; background: #fff; border: 1px solid #ccc; border-radius: 10px; box-shadow: 0 6px 12px rgba(0,0,0,0.1); }"
                +
                ".slip h1, .slip h2 { color: #2c3e50; text-align: center; margin: 10px 0; }" +
                ".slip p { font-size: 16px; color: #34495e; }" +
                ".slip .highlight { font-size: 18px; color: #27ae60; text-align: center; font-weight: bold; margin-top: 15px; }"
                +
                "</style></head><body>" +
                "<div class='slip'><h1>" + companyName + "</h1>" + header +
                "<p><b>Name:</b> " + name + "</p>" +
                "<p><b>Email:</b> " + email + "</p>" +
                "<p><b>Course Name:</b> " + courseName + "</p>" +
                "<p><b>Total Fees:</b> ₹" + totalFees + "</p>" +
                "<p><b>Amount Paid:</b> ₹" + amountPaid + "</p>" +
                "<p><b>Remaining Fees:</b> ₹" + remainingFees + "</p>" +
                "<p class='highlight'>Thank you for enrolling!</p>" +
                "</div></body></html>";
    }

    // private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//
    // GEN-FIRST:event_jButton2ActionPerformed
    // // TODO add your handling code here:
    // AdminDashboard window2 = new AdminDashboard();
    // window2.setVisible(true);
    // this.dispose();
    // }// GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdmissionForm.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdmissionForm.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdmissionForm.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdmissionForm.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            String companyName;

            public void run() {
                new AdmissionForm(companyName).setVisible(true);
            }
        });
    }

    // End of variables declaration
}
