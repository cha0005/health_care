package com.nibm.medlink_healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private ArrayList<Notification> notificationList;
    private FirebaseFirestore db;
    private TextView txtEmptyState;

    // ✅ For returning from detail (removes completed)
    private final ActivityResultLauncher<Intent> detailLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String docId = result.getData().getStringExtra("docId");
                    if (docId != null) {
                        for (int i = 0; i < notificationList.size(); i++) {
                            if (notificationList.get(i).getId().equals(docId)) {
                                notificationList.remove(i);
                                adapter.notifyItemRemoved(i);
                                break;
                            }
                        }
                    }
                }
            });

    // ✅ For returning from CreateReminderActivity (refresh list)
    private final ActivityResultLauncher<Intent> createReminderLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadNotifications();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.recyclerViewNotifications);
        txtEmptyState = findViewById(R.id.txtEmptyState);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(this, notificationList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadNotifications();

        // ✅ launch CreateReminderActivity with launcher
        findViewById(R.id.btnAddReminder).setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateReminderActivity.class);
            createReminderLauncher.launch(intent);
        });

        // Setup the bottom navigation bar
        setupNavigationBar();
    }

    private void loadNotifications() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            txtEmptyState.setVisibility(View.VISIBLE);
            txtEmptyState.setText("Please log in to view reminders.");
            return;
        }

        String userId = user.getUid();

        // ✅ Use a realtime listener so list updates instantly when completion changes
        db.collection("Users")
                .document(userId)
                .collection("Notifications")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        txtEmptyState.setVisibility(View.VISIBLE);
                        txtEmptyState.setText("Failed to load reminders: " + e.getMessage());
                        return;
                    }

                    if (queryDocumentSnapshots == null) return;

                    notificationList.clear();

                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Notification model = snapshot.toObject(Notification.class);
                        model.setId(snapshot.getId());

                        // ✅ Show only *not completed* tasks
                        if (!model.isCompleted()) {
                            notificationList.add(model);
                        }
                    }

                    adapter.notifyDataSetChanged();

                    // ✅ Toggle empty state visibility
                    if (notificationList.isEmpty()) {
                        txtEmptyState.setVisibility(View.VISIBLE);
                        txtEmptyState.setText("You have no pending reminders.");
                    } else {
                        txtEmptyState.setVisibility(View.GONE);
                    }
                });
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

