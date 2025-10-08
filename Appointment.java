package com.nibm.medlink_healthcare;

import com.google.firebase.Timestamp;

public class Appointment {
    private String id;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private Timestamp appointmentTime;

    public Appointment() { } // Firestore requires an empty constructor

    public Appointment(String id, String patientName, String doctorId, String doctorName, Timestamp appointmentTime) {
        this.id = id;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentTime = appointmentTime;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public Timestamp getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(Timestamp appointmentTime) { this.appointmentTime = appointmentTime; }
}
