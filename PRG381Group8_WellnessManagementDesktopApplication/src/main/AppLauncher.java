/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.SwingUtilities;
import view.MainDashboard;

public class AppLauncher {

    public static void main(String[] args) {
        // Launch the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set the Look & Feel (optional, for consistent appearance)
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("Warning: Failed to set Look and Feel");
            }

            // Start the main dashboard
            new MainDashboard().setVisible(true);
        });
    }
}
