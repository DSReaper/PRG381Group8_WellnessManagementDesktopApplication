/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

        /**
     * Adds a new appointment to the database.
     *
     * @param a The appointment object to be added.
     * @return true if the operation is successful; false otherwise.
     */
    public static boolean addAppointment(Appointment a) {
        String sql = "INSERT INTO Appointments (student_name, counsellor_id, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getStudentName());
            ps.setInt(2, a.getCounsellorId());
            ps.setDate(3, Date.valueOf(a.getDate()));
            ps.setTime(4, Time.valueOf(a.getTime()));
            ps.setString(5, a.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding appointment: " + e.getMessage());
            return false;
        }
    }

        /**
     * Retrieves all appointments along with the counsellor's name.
     *
     * @return A list of all appointment records.
     */
    public static List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = """
            SELECT a.id, a.student_name, a.counsellor_id, c.name, a.appointment_date, a.appointment_time, a.status
            FROM Appointments a
            JOIN Counsellors c ON a.counsellor_id = c.id
        """;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Appointment(
                        rs.getInt("id"),
                        rs.getString("student_name"),
                        rs.getInt("counsellor_id"),
                        rs.getString("name"),
                        rs.getDate("appointment_date").toLocalDate(),
                        rs.getTime("appointment_time").toLocalTime(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching appointments: " + e.getMessage());
        }
        return list;
    }

        /**
     * Updates an existing appointment's date, time, and status.
     *
     * @param a The appointment object containing updated values.
     * @return true if update is successful; false otherwise.
     */
    public static boolean updateAppointmentDetails(Appointment a) {
        String sql = """
            UPDATE Appointments 
            SET appointment_date = ?, appointment_time = ?, status = ?
            WHERE id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(a.getDate()));
            ps.setTime(2, java.sql.Time.valueOf(a.getTime()));
            ps.setString(3, a.getStatus());
            ps.setInt(4, a.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        /**
     * Deletes an appointment from the database by ID.
     *
     * @param appointmentId The ID of the appointment to delete.
     * @return true if deletion is successful; false otherwise.
     */
    public static boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM Appointments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
            return false;
        }
    }
 
        /**
     * Checks if a given time slot is available for a counsellor on a specific date.
     *
     * @param counsellorId ID of the counsellor.
     * @param date         Desired appointment date.
     * @param time         Desired appointment time.
     * @return true if the slot is available; false if there is a conflict.
     */
    public static boolean isTimeSlotAvailable(int counsellorId, LocalDate date, LocalTime time) {
        String sql = """
            SELECT COUNT(*) FROM Appointments 
            WHERE counsellor_id = ? 
              AND appointment_date = ? 
              AND appointment_time = ? 
              AND status != 'Cancelled'
        """;

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Convert LocalDate and LocalTime to java.sql.Date and java.sql.Time
            java.sql.Date sqlDate = java.sql.Date.valueOf(date);
            java.sql.Time sqlTime = java.sql.Time.valueOf(time);

            ps.setInt(1, counsellorId);
            ps.setDate(2, sqlDate);
            ps.setTime(3, sqlTime);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // True if no conflict found
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
        /**
     * Checks if a time slot is available for a counsellor on a specific date and time,
     * excluding the current appointment being updated.
     *
     * @param counsellorId        ID of the counsellor.
     * @param date                Desired appointment date.
     * @param time                Desired appointment time.
     * @param excludeAppointmentId ID of the appointment to exclude from conflict check.
     * @return true if the slot is available; false otherwise.
     */
    public static boolean isTimeSlotAvailableExcludingCurrent(int counsellorId, LocalDate date, LocalTime time, int excludeAppointmentId) {
        String sql = """
            SELECT COUNT(*) FROM Appointments 
            WHERE counsellor_id = ? 
              AND appointment_date = ? 
              AND appointment_time = ? 
              AND status != 'Cancelled'
              AND id != ?
        """;

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, counsellorId);
            ps.setDate(2, java.sql.Date.valueOf(date));
            ps.setTime(3, java.sql.Time.valueOf(time));
            ps.setInt(4, excludeAppointmentId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

