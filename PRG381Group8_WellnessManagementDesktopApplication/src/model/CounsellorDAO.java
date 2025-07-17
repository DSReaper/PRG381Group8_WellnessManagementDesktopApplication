/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.DBConnection;
import java.util.List;
import java.util.ArrayList;

/**
 * Data Access Object (DAO) for interacting with the Counsellors table.
 * Handles all CRUD operations and business logic for counsellor data.
 */
public class CounsellorDAO {
    
    /**
     * Adds a new counsellor to the database.
     *
     * @param c Counsellor object containing name, specialization, and availability
     * @return true if insertion is successful, false otherwise
     */
    public boolean addCounsellor(Counsellor c) {
        String sql = "INSERT INTO Counsellors (name, specialization, availability) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getSpecialization());
            ps.setBoolean(3, c.isAvailable());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all counsellors from the database.
     *
     * @return List of Counsellor objects
     */
    public List<Counsellor> getAllCounsellors() {
        List<Counsellor> list = new ArrayList<>();
        String sql = "SELECT * FROM Counsellors";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Counsellor c = new Counsellor(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("specialization"),
                    rs.getBoolean("availability")
                );
                list.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * Updates an existing counsellor's details.
     *
     * @param c Counsellor object containing updated values
     * @return true if update was successful, false otherwise
     */
    public boolean updateCounsellor(Counsellor c) {
        String sql = "UPDATE Counsellors SET name=?, specialization=?, availability=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getSpecialization());
            ps.setBoolean(3, c.isAvailable());
            ps.setInt(4, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * Deletes a counsellor from the database using their ID.
     *
     * @param id Unique counsellor ID
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteCounsellor(int id) {
        String sql = "DELETE FROM Counsellors WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * Retrieves a list of names of all available counsellors.
     *
     * @return List of counsellor names where availability is true
     */
    public List<String> getAvailableCounsellorNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT name FROM Counsellors WHERE availability=true";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return names;
    }
    
    /**
     * Finds a counsellor record by name.
     *
     * @param name The name of the counsellor to search for
     * @return Counsellor object if found, otherwise null
     */
    public Counsellor findByName(String name) {
    String sql = "SELECT * FROM Counsellors WHERE name = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Counsellor(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("specialization"),
                rs.getBoolean("availability")
            );
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return null; // Not found
    }
}
