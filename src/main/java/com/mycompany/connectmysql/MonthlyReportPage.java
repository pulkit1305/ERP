package com.mycompany.connectmysql;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.YearMonth;

public class MonthlyReportPage extends JFrame {
    private JTextField searchField;
    private JTable installmentTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> monthDropdown;
    private JComboBox<String> yearDropdown;
    private JLabel totalStudentsLabel;
    private JLabel paidStudentsLabel;
    private JLabel pendingStudentsLabel;
private final String companyName;
    public MonthlyReportPage(String companyName) {
        this.companyName=companyName;
        setTitle("Installment Report");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Search and Filter Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchPanel.add(new JLabel("Search by Name/UserID: "));
        searchPanel.add(searchField);

        // Month Dropdown
        String[] months = { "All", "January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December" };
        monthDropdown = new JComboBox<>(months);
        searchPanel.add(new JLabel("Select Month: "));
        searchPanel.add(monthDropdown);

        // Year Dropdown
        int currentYear = YearMonth.now().getYear();
        yearDropdown = new JComboBox<>();
        yearDropdown.addItem("All");
        for (int i = currentYear - 5; i <= currentYear + 5; i++) {
            yearDropdown.addItem(String.valueOf(i));
        }
        searchPanel.add(new JLabel("Select Year: "));
        searchPanel.add(yearDropdown);

       
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                loadInstallments();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                loadInstallments();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                loadInstallments();
            }
        });

        monthDropdown.addActionListener(e -> loadInstallments());
        yearDropdown.addActionListener(e -> loadInstallments());

        add(searchPanel, BorderLayout.NORTH);

        // Statistics Panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalStudentsLabel = new JLabel("Total Students: 0");
        paidStudentsLabel = new JLabel("Paid Students: 0");
        pendingStudentsLabel = new JLabel("Pending Students: 0");
        statsPanel.add(totalStudentsLabel);
        statsPanel.add(paidStudentsLabel);
        statsPanel.add(pendingStudentsLabel);
        add(statsPanel, BorderLayout.SOUTH);

        // Table to display installment data
        tableModel = new DefaultTableModel();
        installmentTable = new JTable(tableModel);
        tableModel.setColumnIdentifiers(
                new String[] { "Installment ID", "Student Name", "Course Name", "Amount", "Due Date", "Status" });
        JScrollPane scrollPane = new JScrollPane(installmentTable);
        add(scrollPane, BorderLayout.CENTER);

        loadInstallments();
        setVisible(true);
    }

    private void loadInstallments() {
        tableModel.setRowCount(0); // Clear previous rows
        String searchText = searchField.getText().trim();
        String selectedMonth = (String) monthDropdown.getSelectedItem();
        String selectedYear = (String) yearDropdown.getSelectedItem();

        try {
            // Get company database from DatabaseUtil
            String companyDatabase = companyName;  // Replace with the actual company name
            String databaseName = DatabaseUtil.getCompanyDatabaseName(companyDatabase);

            // Connect to the company database using DatabaseUtil
            Connection conn = DatabaseUtil.connectToCompanyDatabase(databaseName);

            StringBuilder query = new StringBuilder(
                    "SELECT i.id, u.name, c.name, i.installment_amount, i.due_date, " +
                            "CASE WHEN i.paid = 1 THEN 'Paid' " +
                            "WHEN i.advance = 1 THEN 'Paid (Advance)' ELSE 'Pending' END AS status " +
                            "FROM installments i " +
                            "JOIN users u ON i.user_id = u.user_id " +
                            "JOIN courses c ON i.course_id = c.id " +
                            "WHERE (u.name LIKE ? OR u.user_id LIKE ?) ");

            // Append filtering logic for month and year
            if (!"All".equals(selectedMonth) || !"All".equals(selectedYear)) {
                query.append(" AND ");
                if (!"All".equals(selectedMonth)) {
                    query.append("MONTH(i.due_date) = ? ");
                }
                if (!"All".equals(selectedYear)) {
                    if (!"All".equals(selectedMonth))
                        query.append(" AND ");
                    query.append("YEAR(i.due_date) = ? ");
                }
            }

            query.append(" ORDER BY u.name, i.due_date");

            PreparedStatement pst = conn.prepareStatement(query.toString());
            pst.setString(1, "%" + searchText + "%");
            pst.setString(2, "%" + searchText + "%");

            int index = 3;
            if (!"All".equals(selectedMonth)) {
                pst.setInt(index++, monthDropdown.getSelectedIndex());
            }
            if (!"All".equals(selectedYear)) {
                pst.setInt(index, Integer.parseInt(selectedYear));
            }

            ResultSet rs = pst.executeQuery();

            int totalStudents = 0;
            int paidStudents = 0;
            int pendingStudents = 0;

            while (rs.next()) {
                totalStudents++;
                String status = rs.getString("status");
                if ("Paid".equals(status) || "Paid (Advance)".equals(status)) {
                    paidStudents++;
                } else if ("Pending".equals(status)) {
                    pendingStudents++;
                }

                tableModel.addRow(new Object[] {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("c.name"),
                        rs.getDouble("installment_amount"),
                        rs.getDate("due_date"),
                        status
                });
            }

            // Update the statistics labels
            totalStudentsLabel.setText("Total Students: " + totalStudents);
            paidStudentsLabel.setText("Paid Students: " + paidStudents);
            pendingStudentsLabel.setText("Pending Students: " + pendingStudents);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data.");
        }
    }

      public static void main(String[] args) {
     String companyName = "MyCompany"; 
        SwingUtilities.invokeLater(() -> new MonthlyReportPage(companyName));
    }
}
