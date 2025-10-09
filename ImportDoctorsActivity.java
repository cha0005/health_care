package com.nibm.medlink_healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ImportDoctorsActivity extends AppCompatActivity {
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        importDoctorsFromCSV();
    }

    private void importDoctorsFromCSV() {
        try {
            InputStream is = getAssets().open("Doctor_Directory.csv"); // file inside assets
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false; // skip the first row (headers)
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    Map<String, Object> doctor = new HashMap<>();
                    doctor.put("name", parts[0].trim());
                    doctor.put("specialty", parts[1].trim());
                    doctor.put("hospital", parts[2].trim());
                    doctor.put("contact", parts[3].trim());
                    doctor.put("available", parts[4].trim().equalsIgnoreCase("yes"));

                    db.collection("Doctors").add(doctor);
                }
            }

            Toast.makeText(this, "Doctors imported!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
