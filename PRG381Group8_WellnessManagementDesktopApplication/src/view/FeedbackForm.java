package view;

import javax.swing.*;
import java.awt.*;
import controller.FeedbackController;
import util.Validator;

/**
 * Feedback submission form
 */
public class FeedbackForm extends javax.swing.JFrame {

    private JLabel lblRating;
    private JLabel lblComment;
    private JComboBox<Integer> cmbRating;
    private JTextArea txtComment;
    private JButton btnSubmit;
    private JButton btnClear;

    public FeedbackForm() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Submit Feedback");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);

        lblRating = new JLabel("Rating (1-5):");
        lblComment = new JLabel("Comment:");
        cmbRating = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        txtComment = new JTextArea(5, 20);
        txtComment.setLineWrap(true);
        txtComment.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtComment);
        btnSubmit = new JButton("Submit");
        btnClear = new JButton("Clear");

        btnSubmit.addActionListener(e -> handleSubmit());
        btnClear.addActionListener(e -> clearForm());

        // Layout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblRating, gbc);

        gbc.gridx = 1;
        panel.add(cmbRating, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblComment, gbc);

        gbc.gridx = 1;
        panel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(btnClear, gbc);

        gbc.gridx = 1;
        panel.add(btnSubmit, gbc);

        getContentPane().add(panel);
    }

    private void handleSubmit() {
        int rating = (int) cmbRating.getSelectedItem();
        String comment = txtComment.getText().trim();

        // Validate input
        if (!Validator.isValidRating(rating)) {
            JOptionPane.showMessageDialog(this, "Rating must be between 1 and 5.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Validator.isValidComment(comment)) {
            JOptionPane.showMessageDialog(this, "Comment is too long (max 250 characters).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Send to controller
        boolean success = FeedbackController.submitFeedback(rating, comment);
        if (success) {
            JOptionPane.showMessageDialog(this, "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit feedback.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        cmbRating.setSelectedIndex(0);
        txtComment.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeedbackForm().setVisible(true));
    }
}
