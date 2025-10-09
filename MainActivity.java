package com.nibm.medlink_healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private Button btnLogout;
    private LinearLayout btnProfile, btnAppointments, btnRecords, btnNotifications, btnSettings, btnEmergency, btnHealthInsights;
    private FloatingActionButton btnAIChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- Buttons ---
        btnLogout = findViewById(R.id.btnLogout);
        btnProfile = findViewById(R.id.btnProfile);
        btnAppointments = findViewById(R.id.btnAppointments);
        btnRecords = findViewById(R.id.btnRecords);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnSettings = findViewById(R.id.btnSettings);
        btnEmergency = findViewById(R.id.btnEmergency);
        btnAIChat = findViewById(R.id.btnAIChat);
        btnHealthInsights = findViewById(R.id.btnHealthInsights);

        // --- Click actions ---
        btnLogout.setOnClickListener(v -> {
            // Example: go back to LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ProfileActivity.class))
        );

        btnAppointments.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AppointmentsActivity.class))
        );

        btnRecords.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RecordsActivity.class))
        );

        btnNotifications.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, NotificationsActivity.class))
        );

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SettingsActivity.class))
        );

        btnEmergency.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, EmergencyActivity.class))
        );

        btnAIChat.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AIChatActivity.class))
        );

        btnHealthInsights.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, HealthInsightsActivity.class))
        );
    }
}
