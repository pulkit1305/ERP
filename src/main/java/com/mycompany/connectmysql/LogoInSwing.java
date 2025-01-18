/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.connectmysql;

import javax.swing.*;
import java.awt.*;

public class LogoInSwing {
    public static void main(String[] args) {
        // Create a new JFrame
        JFrame frame = new JFrame("Logo Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout()); // Use BorderLayout for positioning

        // Load the image
        ImageIcon originalIcon = new ImageIcon("1089.jpg"); // Provide the path to your image here

        // Resize the image to a smaller size
        Image resizedImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        // Create a JLabel with the resized image
        JLabel logoLabel = new JLabel(resizedIcon);

        // Add the JLabel to the top-left of the frame
        frame.add(logoLabel, BorderLayout.NORTH);

        // Adjust padding by wrapping in a JPanel if needed
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        logoPanel.add(logoLabel);
        frame.add(logoPanel, BorderLayout.NORTH);

        // Make the frame visible
        frame.setVisible(true);
    }
}
