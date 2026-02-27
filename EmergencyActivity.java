package com.nibm.medlink_healthcare;

import static androidx.core.location.LocationManagerCompat.getCurrentLocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

// 🟢 IMPLEMENT OnMapReadyCallback
public class EmergencyActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private Button btnFindHospital;

    private Button btnCallEmergency;
    // Set a universal emergency number. Change this to 999, 119, or a local number if needed.
    private static final String EMERGENCY_NUMBER = "911";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        // 1. Initialize Location Api.Client
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // 2. Initialize and load the Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment); // **IMPORTANT: Ensure this ID matches your XML**

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Map fragment not found in layout.", Toast.LENGTH_LONG).show();
        }

        // 2. Initialize Call Button
        // This assumes your XML uses @+id/btnCallEmergency
        btnCallEmergency = findViewById(R.id.btnCallEmergency);
        if (btnCallEmergency != null) {
            btnCallEmergency.setOnClickListener(v -> handleEmergencyCall());
        }

        btnFindHospital = findViewById(R.id.btnFindHospital);
        if (btnFindHospital != null) {
            btnFindHospital.setOnClickListener(v -> {
                // ✅ Corrected: Use the SAME key you saved in SettingsActivity ("switch_location")
                boolean locationAllowed = getSharedPreferences("AppSettings", MODE_PRIVATE)
                        .getBoolean("switch_location", false);

                if (!locationAllowed) {
                    Toast.makeText(this, "Location access is disabled. Enable it in Settings.", Toast.LENGTH_LONG).show();
                    return;
                }

                // ✅ Open Google Maps with query for nearby hospitals
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=hospitals near me");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(this, "Google Maps is not installed.", Toast.LENGTH_SHORT).show();
                }
            });
        }


        // Setup the bottom navigation bar
        setupNavigationBar();
    }

    // --- MAP IMPLEMENTATION: Called when the map is ready to be used ---
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set map type and initial location (e.g., Colombo, Sri Lanka, as a fallback)
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng defaultLocation = new LatLng(6.9271, 79.8612); // Colombo, Sri Lanka
        mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Fallback Emergency Point"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));

        // Start the process of checking for and requesting location permissions
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                getCurrentLocation(); // Attempt to move to user's location
            }
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null && mMap != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        // Move camera to the user's current location with animation
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
                        Toast.makeText(this, "Location found and map centered.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Could not get current location, showing default.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error getting location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, re-run the check to enable location layer
                checkLocationPermission();
            } else {
                Toast.makeText(this, "Location permission denied. Map functionality is limited.", Toast.LENGTH_LONG).show();
            }
        }
    }


    // --- EMERGENCY CALL IMPLEMENTATION ---
    private void handleEmergencyCall() {
        // ACTION_DIAL opens the phone app with the number pre-filled, which is better
        // than ACTION_CALL as it doesn't require runtime CALL_PHONE permission.
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + EMERGENCY_NUMBER));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Could not open dialer.", Toast.LENGTH_SHORT).show();
        }
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
