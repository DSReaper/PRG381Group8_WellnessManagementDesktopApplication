/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.*;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. Create Counsellors table if not exists
            stmt.executeUpdate("""
                CREATE TABLE Counsellors (
                    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    name VARCHAR(100) NOT NULL,
                    specialization VARCHAR(100),
                    availability BOOLEAN NOT NULL
                )
            """);

        } catch (SQLException e) {
            if (!e.getSQLState().equals("X0Y32")) { // Table already exists
                System.out.println("Error creating Counsellors table: " + e.getMessage());
            }
        }

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // 2. Create Appointments table
            stmt.executeUpdate("""
                CREATE TABLE Appointments (
                    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    student_name VARCHAR(100) NOT NULL,
                    counsellor_id INT NOT NULL,
                    appointment_date DATE NOT NULL,
                    appointment_time TIME NOT NULL,
                    status VARCHAR(20) DEFAULT 'Scheduled',
                    FOREIGN KEY (counsellor_id) REFERENCES Counsellors(id)
                )
            """);

        } catch (SQLException e) {
            if (!e.getSQLState().equals("X0Y32")) {
                System.out.println("Error creating Appointments table: " + e.getMessage());
            }
        }

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // 3. Create Feedback table
            stmt.executeUpdate("""
                CREATE TABLE Feedback (
                    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    student_name VARCHAR(100) NOT NULL,
                    rating INT CHECK (rating >= 1 AND rating <= 5),
                    comments VARCHAR(300)
                )
            """);

        } catch (SQLException e) {
            if (!e.getSQLState().equals("X0Y32")) {
                System.out.println("Error creating Feedback table: " + e.getMessage());
            }
        }

        insertTestData();
    }

    private static void insertTestData() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Counsellors");
            rs.next();
            if (rs.getInt(1) == 0) {
                stmt.executeUpdate("""
                    INSERT INTO Counsellors (name, specialization, availability) VALUES
                    ('Dr. Jane Smith', 'Stress Management', TRUE),
                    ('Mr. Peter Johnson', 'Depression', TRUE),
                    ('Ms. Linda Mokoena', 'Career Guidance', FALSE)
                """);

                stmt.executeUpdate("""
                    INSERT INTO Appointments (student_name, counsellor_id, appointment_date, appointment_time, status) VALUES
                    ('Thabo Nkosi', 1, '2025-07-18', '09:00:00', 'Scheduled'),
                    ('Ayanda Zulu', 2, '2025-07-18', '11:00:00', 'Scheduled')
                """);

                stmt.executeUpdate("""
                    INSERT INTO Feedback (student_name, rating, comments) VALUES
                    ('Thabo Nkosi', 5, 'Excellent support, very professional.'),
                    ('Ayanda Zulu', 4, 'Good advice, helped me a lot.')
                """);

                System.out.println("Test data inserted.");
            } else {
                System.out.println("Tables already contain data. Skipping test insert.");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting test data: " + e.getMessage());
        }
    }
}
