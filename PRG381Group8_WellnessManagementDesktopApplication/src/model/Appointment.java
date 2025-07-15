/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int id;
    private String studentName;
    private int counsellorId;
    private String counsellorName;
    private LocalDate date;
    private LocalTime time;
    private String status;

    public Appointment(int id, String studentName, int counsellorId, String counsellorName, LocalDate date, LocalTime time, String status) {
        this.id = id;
        this.studentName = studentName;
        this.counsellorId = counsellorId;
        this.counsellorName = counsellorName;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Appointment(String studentName, int counsellorId, LocalDate date, LocalTime time, String status) {
        this.studentName = studentName;
        this.counsellorId = counsellorId;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public int getCounsellorId() { return counsellorId; }
    public void setCounsellorId(int counsellorId) { this.counsellorId = counsellorId; }
    public String getCounsellorName() { return counsellorName; }
    public void setCounsellorName(String counsellorName) { this.counsellorName = counsellorName; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

