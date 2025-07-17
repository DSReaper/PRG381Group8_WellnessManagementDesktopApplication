/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Represents a counsellor entity with relevant attributes.
 * This class is used to store and retrieve counsellor information,
 * including their availability for appointments.
 */
public class Counsellor {
    private int id;
    private String name;
    private String specialization;
    private boolean availability;

    /**
     * Full constructor for Counsellor including ID.
     * Useful when retrieving counsellor records from the database.
     *
     * @param id              Unique counsellor ID
     * @param name            Counsellor's full name
     * @param specialization  Area of expertise or counselling focus
     * @param availability    Indicates if the counsellor is currently available
     */
    public Counsellor(int id, String name, String specialization, boolean availability) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.availability = availability;
    }

    /**
     * Constructor without ID, used when creating a new counsellor
     * before saving to the database (where the ID is auto-generated).
     *
     * @param name            Counsellor's full name
     * @param specialization  Area of expertise or counselling focus
     * @param availability    Indicates if the counsellor is currently available
     */
    public Counsellor(String name, String specialization, boolean availability) {
        this.name = name;
        this.specialization = specialization;
        this.availability = availability;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public boolean isAvailable() { return availability; }
    public void setAvailability(boolean availability) { this.availability = availability; }

    @Override
    public String toString() {
        return name + " - " + specialization + (availability ? " (Available)" : " (Unavailable)");
    }
}

