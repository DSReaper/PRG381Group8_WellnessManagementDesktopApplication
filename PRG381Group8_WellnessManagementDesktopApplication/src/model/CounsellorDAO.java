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

public class CounsellorDAO {
    public boolean addCounsellor(Counsellor c) {
        String sql = "INSERT INTO Counselors (name, specialization, availability) VALUES (?, ?, ?)";
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

    public List<Counsellor> getAllCounsellors() {
        List<Counsellor> list = new ArrayList<>();
        String sql = "SELECT * FROM Counselors";
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

    public boolean updateCounsellor(Counsellor c) {
        String sql = "UPDATE Counselors SET name=?, specialization=?, availability=? WHERE id=?";
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

    public boolean deleteCounsellor(int id) {
        String sql = "DELETE FROM Counselors WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public List<String> getAvailableCounsellorNames() {
    List<String> names = new ArrayList<>();
    String sql = "SELECT name FROM Counselors WHERE availability=true";
    try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            names.add(rs.getString("name"));
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return names;
}
    public Counsellor findByName(String name) {
    String sql = "SELECT * FROM Counselors WHERE name = ?";
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
