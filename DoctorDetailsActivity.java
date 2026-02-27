package com.nibm.medlink_healthcare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class DoctorDetailsActivity extends AppCompatActivity {

    private TextView txtName, txtSpecialty, txtHospital, txtContact, txtAvailable;
    private Button btnBook;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        txtName = findViewById(R.id.txtName);
        txtSpecialty = findViewById(R.id.txtSpecialty);
        txtHospital = findViewById(R.id.txtHospital);
        txtContact = findViewById(R.id.txtContact);
        txtAvailable = findViewById(R.id.txtAvailable);
        btnBook = findViewById(R.id.btnBook);

        db = FirebaseFirestore.getInstance();

        // Read extras safely
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "No doctor data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final String doctorId = extras.getString("id", "");
        final String name = extras.getString("name", "N/A");
        final String specialty = extras.getString("specialty", "N/A");
        final String hospital = extras.getString("hospital", "N/A");
        final String contact = extras.getString("contact", "N/A");
        final boolean available = extras.getBoolean("available", false);

        txtName.setText(name);
        txtSpecialty.setText(specialty);
        txtHospital.setText(hospital);
        txtContact.setText(contact);
        txtAvailable.setText(available ? "Available" : "Not available");

        btnBook.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(this, "Please log in to book", Toast.LENGTH_SHORT).show();
                return;
            }

            btnBook.setEnabled(false);

            // Create appointment entry
            Map<String, Object> appointment = new HashMap<>();
            appointment.put("doctorId", doctorId);
            appointment.put("doctorName", name);
            appointment.put("userId", user.getUid());
            appointment.put("userEmail", user.getEmail());
            appointment.put("createdAt", FieldValue.serverTimestamp());

            db.collection("Appointments")
                    .add(appointment)
                    .addOnSuccessListener(docRef -> {
                        // ✅ Update doctor availability in Firestore
                        db.collection("Doctors")
                                .document(doctorId)
                                .update("available", true)
                                .addOnSuccessListener(aVoid -> {
                                    txtAvailable.setText("Available");
                                    txtAvailable.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                    Toast.makeText(this, "Appointment booked!", Toast.LENGTH_SHORT).show();
                                    btnBook.setEnabled(true);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to update availability: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    btnBook.setEnabled(true);
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Booking failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        btnBook.setEnabled(true);
                    });
        });

    }
}
