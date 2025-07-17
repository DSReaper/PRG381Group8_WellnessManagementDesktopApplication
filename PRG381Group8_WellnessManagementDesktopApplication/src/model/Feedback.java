/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Model class representing Feedback.
 */
public class Feedback {
    private int id;
    private String studentName;
    private int rating; // 1 to 5
    private String comment;

    // Constructors
    public Feedback() {}

    public Feedback(String studentName, int rating, String comment) {
        this.studentName = studentName;
        this.rating = rating;
        this.comment = comment;
    }

    public Feedback(int id, String studentName, int rating, String comment) {
        this.id = id;
        this.studentName = studentName;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
