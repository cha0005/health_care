package com.nibm.medlink_healthcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class RecordsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FileAdapter fileAdapter;
    ArrayList<FileModel> fileList;

    Button btnUpload;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    ActivityResultLauncher<String> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        recyclerView = findViewById(R.id.recyclerViewFiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fileList = new ArrayList<>();
        fileAdapter = new FileAdapter(this, fileList);
        recyclerView.setAdapter(fileAdapter);

        btnUpload = findViewById(R.id.btnUpload);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // File picker
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        saveFileMetadata(uri);
                    }
                });

        btnUpload.setOnClickListener(v -> filePickerLauncher.launch("*/*"));

        // Load existing records
        fetchFiles();

        // Setup the bottom navigation bar
        setupNavigationBar();
    }

    private void saveFileMetadata(Uri fileUri) {
        String userId = mAuth.getCurrentUser().getUid();
        String fileName = System.currentTimeMillis() + "_" + fileUri.getLastPathSegment();

        FileModel fileModel = new FileModel(fileName, fileUri.toString(), System.currentTimeMillis());

        db.collection("Users")
                .document(userId)
                .collection("MedicalRecords")
                .add(fileModel)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "File metadata saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void fetchFiles() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("Users")
                .document(userId)
                .collection("MedicalRecords")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    fileList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        FileModel file = doc.toObject(FileModel.class);
                        file.setId(doc.getId());
                        fileList.add(file);
                    }
                    fileAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load files", Toast.LENGTH_SHORT).show());
    }

    private void setupNavigationBar() {
        // Find all navigation items
        ImageView navHome = findViewById(R.id.navHome);
        ImageView navProfile = findViewById(R.id.navProfile);
        ImageView navAppointments = findViewById(R.id.navAppointments);
        ImageView navRecords = findViewById(R.id.navRecords);
        ImageView navNotifications = findViewById(R.id.navNotifications);
        ImageView navSettings = findViewById(R.id.navSettings);
        ImageView navEmergency = findViewById(R.id.navEmergency);
        ImageView navAI = findViewById(R.id.navAI);

        // 1. Reset all to normal background (assuming R.drawable.nav_circle_bg is defined)
        resetNavStyles(navHome, navProfile, navAppointments, navRecords,
                navNotifications, navSettings, navEmergency, navAI);

        // 2. Highlight the current page (Emergency)
        // Assuming R.drawable.nav_circle_active is the background for the active item
        navEmergency.setBackgroundResource(R.drawable.nav_circle_active);

        // 3. Set click listeners for navigation
        navHome.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        navAppointments.setOnClickListener(v -> startActivity(new Intent(this, AppointmentsActivity.class)));
        navRecords.setOnClickListener(v -> startActivity(new Intent(this, RecordsActivity.class)));
        navNotifications.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));
        navSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        // Emergency button action (stay on current screen)
        navEmergency.setOnClickListener(v ->
                Toast.makeText(this, "You're already on Emergency", Toast.LENGTH_SHORT).show());

        navAI.setOnClickListener(v -> startActivity(new Intent(this, AIChatActivity.class)));
    }

    /**
     * Resets the background style for all navigation ImageViews.
     */
    private void resetNavStyles(ImageView... navItems) {
        for (ImageView item : navItems) {
            item.setBackgroundResource(R.drawable.nav_circle_bg);
        }
    }
}
