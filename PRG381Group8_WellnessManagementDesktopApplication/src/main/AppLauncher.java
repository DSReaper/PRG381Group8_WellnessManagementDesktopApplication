/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import model.DBConnection;
import view.MainDashboard;
import java.sql.Connection;

public class AppLauncher {

    public static void main(String[] args) {
        // Run GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set the system Look & Feel
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("Warning: Failed to set Look and Feel.");
            }

            // Check DB Connection before launching app
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("Database connected successfully.");
                new MainDashboard().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Failed to connect to the database.\nPlease make sure JavaDB is running.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
