package com.nibm.medlink_healthcare;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent; //Navigation bar logic
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerDoctors;
    private FirebaseFirestore db;
    private List<Doctor> doctorList;
    private DoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        recyclerDoctors = findViewById(R.id.recyclerDoctors);
        recyclerDoctors.setLayoutManager(new LinearLayoutManager(this));

        doctorList = new ArrayList<>();
        adapter = new DoctorAdapter(this, doctorList);  // <-- context first, list second
        recyclerDoctors.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadDoctors();
    }

    private void loadDoctors() {
        db.collection("Doctors")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    doctorList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Doctor doctor = new Doctor();
                        doctor.setId(doc.getId());
                        doctor.setName(doc.getString("name"));
                        doctor.setSpecialty(doc.getString("specialty"));
                        doctor.setHospital(doc.getString("hospital"));
                        doctor.setContact(doc.getString("contact"));

                        // Handle available safely
                        Object avail = doc.get("available");
                        if (avail instanceof Boolean) {
                            doctor.setAvailable((Boolean) avail);
                        } else if (avail instanceof String) {
                            doctor.setAvailable(((String) avail).equalsIgnoreCase("yes") ||
                                    ((String) avail).equalsIgnoreCase("true"));
                        } else {
                            doctor.setAvailable(false);
                        }

                        doctorList.add(doctor);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );

        setupNavBar();
    }

    private void setupNavBar() {
        ImageView navHome = findViewById(R.id.navHome);
        ImageView navProfile = findViewById(R.id.navProfile);
        ImageView navAppointments = findViewById(R.id.navAppointments);
        ImageView navRecords = findViewById(R.id.navRecords);
        ImageView navNotifications = findViewById(R.id.navNotifications);
        ImageView navSettings = findViewById(R.id.navSettings);
        ImageView navEmergency = findViewById(R.id.navEmergency);
        ImageView navAI = findViewById(R.id.navAI);

        // reset all to normal background
        resetNavStyles(navHome, navProfile, navAppointments, navRecords,
                navNotifications, navSettings, navEmergency, navAI);

        // highlight the current page (Appointments)
        navAppointments.setBackgroundResource(R.drawable.nav_circle_active);

        navHome.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        navAppointments.setOnClickListener(v ->
                Toast.makeText(this, "You're already on Appointments", Toast.LENGTH_SHORT).show());
        navRecords.setOnClickListener(v -> startActivity(new Intent(this, RecordsActivity.class)));
        navNotifications.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));
        navSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        navEmergency.setOnClickListener(v -> startActivity(new Intent(this, EmergencyActivity.class)));
        navAI.setOnClickListener(v -> startActivity(new Intent(this, AIChatActivity.class)));
    }

    private void resetNavStyles(ImageView... navItems) {
        for (ImageView item : navItems) {
            item.setBackgroundResource(R.drawable.nav_circle_bg);
        }
    }


}
