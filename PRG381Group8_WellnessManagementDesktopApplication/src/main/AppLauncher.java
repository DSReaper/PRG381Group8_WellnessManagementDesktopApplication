/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import model.DBConnection;
import view.MainDashBoard;
import java.sql.Connection;
import model.DatabaseInitializer;

public class AppLauncher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("Warning: Failed to set Look and Feel.");
            }

            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("Database connected.");
                DatabaseInitializer.initialize(); // ← initialize schema & test data
                new MainDashBoard().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Could not connect to database.",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
