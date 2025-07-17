/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents an appointment between a student and a counsellor.
 * Contains the necessary information to store, retrieve, and update appointments.
 */
public class Appointment {
    private int id;
    private String studentName;
    private int counsellorId;
    private String counsellorName;
    private LocalDate date;
    private LocalTime time;
    private String status;

    /**
     * Full constructor used when all appointment details including ID and counsellor name are known.
     *
     * @param id             Unique appointment ID.
     * @param studentName    Name of the student.
     * @param counsellorId   ID of the counsellor.
     * @param counsellorName Name of the counsellor.
     * @param date           Date of the appointment.
     * @param time           Time of the appointment.
     * @param status         Status of the appointment (e.g., Scheduled, Completed, Cancelled).
     */
    public Appointment(int id, String studentName, int counsellorId, String counsellorName, LocalDate date, LocalTime time, String status) {
        this.id = id;
        this.studentName = studentName;
        this.counsellorId = counsellorId;
        this.counsellorName = counsellorName;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    /**
     * Constructor used for creating new appointments before the database generates the ID.
     *
     * @param studentName   Name of the student.
     * @param counsellorId  ID of the counsellor.
     * @param date          Date of the appointment.
     * @param time          Time of the appointment.
     * @param status        Status of the appointment.
     */
    public Appointment(String studentName, int counsellorId, LocalDate date, LocalTime time, String status) {
        this.studentName = studentName;
        this.counsellorId = counsellorId;
        this.date = date;
        this.time = time;
        this.status = status;
    }
   
    /**
     * Default no-argument constructor.
     * Useful when creating an Appointment object for partial updates (e.g., updating status only).
     */
    public Appointment() {
    // No-arg constructor for partial updates (e.g., update status, date, and time)
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

