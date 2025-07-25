/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
import model.Appointment;
import model.AppointmentDAO;
import model.Counsellor;
import model.CounsellorDAO;
import util.DialogUtil;
import util.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;

public class AppointmentForm extends javax.swing.JFrame {
    
    private final CounsellorDAO counsellorDAO = new CounsellorDAO();
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AppointmentForm.class.getName());
    private DefaultTableModel tableModel;
    private int selectedAppointmentId = -1;
    private MainDashBoard dashboard;
    
    // Constructor: Initializes UI, table, and loads data
    public AppointmentForm(MainDashBoard dashboard) {
        initComponents();
        setTitle("Appointments");
        setLocationRelativeTo(null);
        this.dashboard = dashboard;
        setupTable();
        loadStatuses();   
        loadCounsellors();
        loadAppointments();
        addActionListeners();
    }
    
    // Configure table columns
    private void setupTable() {
        tableModel = new DefaultTableModel(new Object[]{"ID", "Student", "Counsellor", "Date", "Time", "Status"}, 0);
        tblAppointments.setModel(tableModel);
    }
    
    // Load status values into the status combo box
    private void loadStatuses() {
        cmbStatus.removeAllItems();
        cmbStatus.addItem("Scheduled");
        cmbStatus.addItem("Completed");
        cmbStatus.addItem("Cancelled");
    }
    
    // Load available counsellors into the combo box
    private void loadCounsellors() {
        cmbCounsellors.removeAllItems();
        List<Counsellor> counsellors = counsellorDAO.getAllCounsellors();
        for (Counsellor c : counsellors) {
            if (c.isAvailable()) {
                cmbCounsellors.addItem(c.getName());
            }
        }
    }
    
    // Load appointments into the table
    private void loadAppointments() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = AppointmentDAO.getAllAppointments();
        for (Appointment a : appointments) {
            tableModel.addRow(new Object[]{
                    a.getId(), a.getStudentName(), a.getCounsellorName(),
                    a.getDate(), a.getTime(), a.getStatus()
            });
        }
    }

    // Add listeners to buttons and table selection
    private void addActionListeners() {
        btnBook.addActionListener(e -> bookAppointment());
        btnUpdate.addActionListener(e -> updateAppointment()); // This now handles the update
        btnCancel.addActionListener(e -> deleteAppointment());
        jButton4.addActionListener(e -> clearForm());

        // Populate form fields when selecting a row in the table
        tblAppointments.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedForUpdate();
            }
        });
    }

    // Book new appointment or update existing one
    private void bookAppointment() {
        String student = txtStudentName.getText().trim();
        String dateStr = txtDate.getText().trim();
        String timeStr = jTextField1.getText().trim();
        String counsellorName = (String) cmbCounsellors.getSelectedItem();
        String status = (String) cmbStatus.getSelectedItem();

         // Validate input fields
        if (!Validator.isNotEmpty(student) || !Validator.isNotEmpty(dateStr) || !Validator.isNotEmpty(timeStr) || counsellorName == null) {
            DialogUtil.showError("Please fill in all fields.");
            return;
        }

        if (!Validator.isValidDate(dateStr)) {
            DialogUtil.showError("Date must be in format YYYY-MM-DD.");
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            DialogUtil.showError("Invalid date format.");
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            DialogUtil.showError("Appointment date cannot be in the past.");
            return;
        }

        if (!Validator.isValidTime(timeStr)) {
            DialogUtil.showError("Time must be in format HH:MM and within 00:00–23:59.");
            return;
        }

        LocalTime time;
        try {
            time = LocalTime.parse(timeStr);
        } catch (DateTimeParseException e) {
            DialogUtil.showError("Invalid time format.");
            return;
        }

        Counsellor selected = counsellorDAO.findByName(counsellorName);
        if (selected == null) {
            DialogUtil.showError("Counsellor not found.");
            return;
        }

        boolean result;

        if (selectedAppointmentId == -1) {
            // Booking new appointment
            if (!AppointmentDAO.isTimeSlotAvailable(selected.getId(), date, time)) {
                DialogUtil.showError("This counsellor already has an appointment at the selected time.");
                return;
            }

            Appointment newAppointment = new Appointment(student, selected.getId(), date, time, status);
            result = AppointmentDAO.addAppointment(newAppointment);

            if (result) {
                DialogUtil.showInfo("Appointment booked successfully.");
            } else {
                DialogUtil.showError("Error booking appointment.");
            }

        } else {
            // Updating existing appointment: only update status, date, and time
            Appointment updatedAppointment = new Appointment();
            updatedAppointment.setId(selectedAppointmentId);
            updatedAppointment.setStatus(status);
            updatedAppointment.setDate(date);
            updatedAppointment.setTime(time);

            result = AppointmentDAO.updateAppointmentDetails(updatedAppointment);

            if (result) {
                DialogUtil.showInfo("Appointment updated successfully.");
            } else {
                DialogUtil.showError("Error updating appointment.");
            }
        }

        if (result) {
            loadAppointments();
            clearForm();
        }
    }

     // Load selected appointment into form fields
    private void loadSelectedForUpdate() {
        int row = tblAppointments.getSelectedRow();
        if (row != -1) {
            selectedAppointmentId = (int) tableModel.getValueAt(row, 0);
            txtStudentName.setText(tableModel.getValueAt(row, 1).toString());
            cmbCounsellors.setSelectedItem(tableModel.getValueAt(row, 2).toString());
            txtDate.setText(tableModel.getValueAt(row, 3).toString());
            jTextField1.setText(tableModel.getValueAt(row, 4).toString());
            cmbStatus.setSelectedItem(tableModel.getValueAt(row, 5).toString());
        }
    }

    // Delete selected appointment permanently
    private void deleteAppointment() {
        int row = tblAppointments.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            if (DialogUtil.confirm("Are you sure you want to permanently delete this appointment?")) {
                if (AppointmentDAO.deleteAppointment(id)) {
                    DialogUtil.showInfo("Appointment deleted successfully.");
                    loadAppointments();
                    clearForm();
                } else {
                    DialogUtil.showError("Failed to delete appointment.");
                }
            }
        } else {
            DialogUtil.showError("Please select an appointment to delete.");
        }
    }
    
     // Update appointment's status, date, and time — not counsellor
    private void updateAppointment() {
        if (selectedAppointmentId == -1) {
            DialogUtil.showError("Please select an appointment from the table first.");
            return;
        }

        String status = (String) cmbStatus.getSelectedItem();
        String dateText = txtDate.getText().trim();
        String timeText = jTextField1.getText().trim();
        String counsellorName = (String) cmbCounsellors.getSelectedItem();

        if (!Validator.isNotEmpty(status) || !Validator.isNotEmpty(dateText) || !Validator.isNotEmpty(timeText)) {
            DialogUtil.showError("Please fill in all fields (status, date, and time).");
            return;
        }

        if (!Validator.isValidDate(dateText)) {
            DialogUtil.showError("Date must be in format YYYY-MM-DD.");
            return;
        }

        LocalDate date = LocalDate.parse(dateText);
        if (date.isBefore(LocalDate.now())) {
            DialogUtil.showError("Appointment date cannot be in the past.");
            return;
        }

        if (!Validator.isValidTime(timeText)) {
            DialogUtil.showError("Time must be in format HH:MM (00:00 to 23:59).");
            return;
        }

        LocalTime time = LocalTime.parse(timeText);
        Counsellor selected = counsellorDAO.findByName(counsellorName);
        if (selected == null) {
            DialogUtil.showError("Counsellor not found.");
            return;
        }

        // Check if updated time slot is available
        boolean isAvailable = AppointmentDAO.isTimeSlotAvailableExcludingCurrent(selected.getId(), date, time, selectedAppointmentId);
        if (!isAvailable) {
            DialogUtil.showError("Selected time slot is already booked with this counsellor.");
            return;
        }

        // Perform update
        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setId(selectedAppointmentId);
        updatedAppointment.setDate(date);
        updatedAppointment.setTime(time);
        updatedAppointment.setStatus(status);

        boolean success = AppointmentDAO.updateAppointmentDetails(updatedAppointment);
        if (success) {
            DialogUtil.showInfo("Appointment updated successfully.");
            loadAppointments();
            clearForm();
        } else {
            DialogUtil.showError("Failed to update appointment.");
        }
    }
    
    // Clear form inputs and reset state
    private void clearForm() {
        txtStudentName.setText("");
        txtDate.setText("YYYY-MM-DD");
        jTextField1.setText("HH:MM");

        if (cmbCounsellors.getItemCount() > 0) {
            cmbCounsellors.setSelectedIndex(0);
        }

        if (cmbStatus.getItemCount() > 0) {
            cmbStatus.setSelectedIndex(0);
        }

        selectedAppointmentId = -1;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblAppointments = new javax.swing.JTable();
        btnBook = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        lblStudentName = new javax.swing.JLabel();
        txtStudentName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        txtDate = new javax.swing.JTextField();
        cmbCounsellors = new javax.swing.JComboBox<>();
        lblCounsellors = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblAppointments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Student", "Counsellor", "Date", "Time", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAppointments.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblAppointments);
        if (tblAppointments.getColumnModel().getColumnCount() > 0) {
            tblAppointments.getColumnModel().getColumn(0).setResizable(false);
            tblAppointments.getColumnModel().getColumn(1).setResizable(false);
            tblAppointments.getColumnModel().getColumn(2).setResizable(false);
            tblAppointments.getColumnModel().getColumn(3).setResizable(false);
            tblAppointments.getColumnModel().getColumn(4).setResizable(false);
            tblAppointments.getColumnModel().getColumn(5).setResizable(false);
        }

        btnBook.setText("Book Appointment");

        btnUpdate.setText("Update Appointment");

        btnCancel.setText("Delete Appointment");

        jButton4.setText("Clear");

        lblStudentName.setText("Student Name:");

        jLabel1.setText("Date:");

        lblTime.setText("Time:");

        lblCounsellors.setText("Counsellors:");

        lblStatus.setText("Status:");

        btnBack.setText("Back to Dashboard");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBook)
                        .addGap(33, 33, 33)
                        .addComponent(btnUpdate)
                        .addGap(28, 28, 28)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStudentName)
                            .addComponent(jLabel1)
                            .addComponent(lblTime)
                            .addComponent(lblCounsellors)
                            .addComponent(lblStatus))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbCounsellors, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtStudentName)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                            .addComponent(txtDate)
                            .addComponent(cmbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBack)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblStudentName)
                            .addComponent(txtStudentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnBack)))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTime)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCounsellors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCounsellors))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBook)
                    .addComponent(btnUpdate)
                    .addComponent(btnCancel)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();  // Close current form
        if (dashboard != null) {
            dashboard.setVisible(true);   // Show dashboard again
            dashboard.setState(JFrame.NORMAL);
            dashboard.toFront();
        }
    }//GEN-LAST:event_btnBackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBook;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbCounsellors;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblCounsellors;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStudentName;
    private javax.swing.JLabel lblTime;
    private javax.swing.JTable tblAppointments;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtStudentName;
    // End of variables declaration//GEN-END:variables
}
