package com.nibm.medlink_healthcare;

public class Doctor {
    private String id;
    private String name;
    private String specialty;
    private String hospital;
    private String contact;
    private boolean available;

    public Doctor() {}  // Firestore needs empty constructor

    public Doctor(String id, String name, String specialty, String hospital, String contact, boolean available) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.hospital = hospital;
        this.contact = contact;
        this.available = available;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getHospital() { return hospital; }
    public void setHospital(String hospital) { this.hospital = hospital; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
