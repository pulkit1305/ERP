package com.mycompany.connectmysql;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageCourses extends JFrame {
    private Connection con;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, feesField;
    private JTextArea descriptionArea;
    private String companyDatabase;
    private static String CompanyName;

    public ManageCourses(String CompanyName) {
        this.CompanyName = CompanyName;
        setCompanyDatabase(CompanyName);

        setTitle("Manage Courses");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        connectToDatabase();

        // Panel for form inputs
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add/Edit/Delete Course"));

        formPanel.add(new JLabel("Course Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descriptionArea));

        formPanel.add(new JLabel("Fees:"));
        feesField = new JTextField();
        formPanel.add(feesField);

        JButton addButton = new JButton("Add Course");
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        formPanel.add(addButton);
        formPanel.add(editButton);
        formPanel.add(deleteButton);

        // Add Back button with smaller font and position it at the top-left
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 12)); // Smaller font size
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.add(backButton);

        // Table to display courses
        tableModel = new DefaultTableModel(new Object[] { "ID", "Name", "Description", "Fees" }, 0);
        table = new JTable(tableModel);
        loadCourses();

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Courses"));

        // Add action listeners
        addButton.addActionListener(e -> addCourse());
        editButton.addActionListener(e -> editCourse());
        deleteButton.addActionListener(e -> deleteCourse());

        // Back button action listener
        backButton.addActionListener(e -> {
            // Close the current window and open AdminDashboard
            dispose(); // Close current window
            new AdminDashboard(CompanyName).setVisible(true); // Open AdminDashboard (adjust if needed)
        });

        // Add selection listener for autofill
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                populateFields();
            }
        });

        // Layout setup
        setLayout(new BorderLayout());

        // Create a wrapper panel to hold both the back button and form
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backPanel, BorderLayout.WEST);
        topPanel.add(formPanel, BorderLayout.CENTER);

        // Add the wrapper panel to the top (NORTH)
        add(topPanel, BorderLayout.NORTH);

        // Table in the center
        add(tableScrollPane, BorderLayout.CENTER);

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

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            if (companyDatabase != null) {
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/" + companyDatabase + "?zeroDateTimeBehavior=CONVERT_TO_NULL",
                        "springstudent", "springstudent");
            } else {
                throw new Exception("Company database is not set. Please ensure company selection is done first.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to company database!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCourses() {
        try {
            tableModel.setRowCount(0);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM courses");

            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("fees")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load courses!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCourse() {
        String name = nameField.getText();
        String description = descriptionArea.getText();
        String feesText = feesField.getText();

        if (name.isEmpty() || description.isEmpty() || feesText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int fees = Integer.parseInt(feesText);

            PreparedStatement ps = con
                    .prepareStatement("INSERT INTO courses (name, description, fees) VALUES (?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, fees);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Course added successfully!");
            loadCourses();

            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Fees must be a valid number!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add course!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editCourse() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to edit!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        String description = descriptionArea.getText();
        String feesText = feesField.getText();

        if (name.isEmpty() || description.isEmpty() || feesText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int fees = Integer.parseInt(feesText);

            PreparedStatement ps = con
                    .prepareStatement("UPDATE courses SET name = ?, description = ?, fees = ? WHERE id = ?");
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, fees);
            ps.setInt(4, id);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Course updated successfully!");
            loadCourses();

            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Fees must be a valid number!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update course!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCourse() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this course?", "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM courses WHERE id = ?");
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Course deleted successfully!");
                loadCourses();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete course!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFields() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            descriptionArea.setText((String) tableModel.getValueAt(selectedRow, 2));
            feesField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
        }
    }

    private void clearFields() {
        nameField.setText("");
        descriptionArea.setText("");
        feesField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManageCourses manageCourses = new ManageCourses(CompanyName);
            manageCourses.setVisible(true);
        });
    }
}
