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
import java.util.List;
import java.util.logging.Logger;

public class AppointmentForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AppointmentForm.class.getName());
    private DefaultTableModel tableModel;
    private int selectedAppointmentId = -1;
    /**
     * Creates new form AppointmentForm
     */
    public AppointmentForm() {
        initComponents();
        setLocationRelativeTo(null);
        setupTable();
        loadCounsellors();
        loadAppointments();
        addActionListeners();
    }
    
    private void setupTable() {
        tableModel = new DefaultTableModel(new Object[]{"ID", "Student", "Counsellor", "Date", "Time", "Status"}, 0);
        tblAppointments.setModel(tableModel);
    }

    private void loadCounsellors() {
        cmbCounsellors.removeAllItems();
        List<Counsellor> counsellors = CounsellorDAO.getAllCounsellors();
        for (Counsellor c : counsellors) {
            if (c.isAvailable()) {
                cmbCounsellors.addItem(c.getName());
            }
        }
    }

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

    private void addActionListeners() {
        btnBook.addActionListener(e -> bookAppointment());
        btnUpdate.addActionListener(e -> loadSelectedForUpdate());
        btnCancel.addActionListener(e -> cancelAppointment());
        jButton4.addActionListener(e -> clearForm());
    }

    private void bookAppointment() {
        String student = txtStudentName.getText().trim();
        String date = txtDate.getText().trim();
        String time = jTextField1.getText().trim();
        String counsellorName = (String) cmbCounsellors.getSelectedItem();
        String status = (String) cmbStatus.getSelectedItem();

        if (!Validator.isNotEmpty(student) || !Validator.isNotEmpty(date) || !Validator.isNotEmpty(time) || counsellorName == null) {
            DialogUtil.showError("Please fill in all fields.");
            return;
        }

        if (!Validator.isValidDate(date)) {
            DialogUtil.showError("Date must be in format YYYY-MM-DD.");
            return;
        }

        if (!Validator.isValidTime(time)) {
            DialogUtil.showError("Time must be in format HH:MM.");
            return;
        }

        Counsellor selected = CounsellorDAO.findByName(counsellorName);
        if (selected == null) {
            DialogUtil.showError("Counsellor not found.");
            return;
        }

        Appointment a = new Appointment(student, selected.getId(),
                LocalDate.parse(date), LocalTime.parse(time), status);

        boolean result;
        if (selectedAppointmentId == -1) {
            result = AppointmentDAO.addAppointment(a);
            if (result) DialogUtil.showInfo("Appointment booked successfully.");
        } else {
            a.setId(selectedAppointmentId);
            result = AppointmentDAO.updateStatus(a.getId(), status);
            if (result) DialogUtil.showInfo("Appointment updated successfully.");
        }

        if (result) {
            loadAppointments();
            clearForm();
        } else {
            DialogUtil.showError("Error saving appointment.");
        }
    }

    private void loadSelectedForUpdate() {
        int row = tblAppointments.getSelectedRow();
        if (row != -1) {
            selectedAppointmentId = (int) tableModel.getValueAt(row, 0);
            txtStudentName.setText(tableModel.getValueAt(row, 1).toString());
            cmbCounsellors.setSelectedItem(tableModel.getValueAt(row, 2).toString());
            txtDate.setText(tableModel.getValueAt(row, 3).toString());
            jTextField1.setText(tableModel.getValueAt(row, 4).toString());
            cmbStatus.setSelectedItem(tableModel.getValueAt(row, 5).toString());
        } else {
            DialogUtil.showError("Please select an appointment to update.");
        }
    }

    private void cancelAppointment() {
        int row = tblAppointments.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            if (DialogUtil.confirm("Are you sure you want to cancel this appointment?")) {
                if (AppointmentDAO.updateStatus(id, "Cancelled")) {
                    DialogUtil.showInfo("Appointment cancelled.");
                    loadAppointments();
                } else {
                    DialogUtil.showError("Failed to cancel appointment.");
                }
            }
        } else {
            DialogUtil.showError("Please select an appointment to cancel.");
        }
    }

    private void clearForm() {
        txtStudentName.setText("");
        txtDate.setText("YYYY-MM-DD");
        jTextField1.setText("HH:MM");
        cmbCounsellors.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
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

        btnCancel.setText("Cancel Appointment");

        jButton4.setText("Clear");

        lblStudentName.setText("Student Name:");

        jLabel1.setText("Date:");

        lblTime.setText("Time:");

        cmbCounsellors.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblCounsellors.setText("Counsellors:");

        lblStatus.setText("Status:");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
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
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStudentName)
                    .addComponent(txtStudentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new AppointmentForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
