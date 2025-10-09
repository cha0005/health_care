package com.nibm.medlink_healthcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat switchAlarm, switchLocation, switchCalls, switchNotification, switchSharing;
    private Button buttonHelpSupport, buttonDeleteAccount;
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // ✅ Bind switches
        switchAlarm = findViewById(R.id.switch_alarm);
        switchLocation = findViewById(R.id.switch_location);
        switchCalls = findViewById(R.id.switch_calls);
        switchNotification = findViewById(R.id.switch_notification);
        switchSharing = findViewById(R.id.switch_sharing);

        // ✅ Bind buttons
        buttonHelpSupport = findViewById(R.id.button_help_support);
        buttonDeleteAccount = findViewById(R.id.button_delete_account);

        // Load saved states
        switchAlarm.setChecked(preferences.getBoolean("switch_alarm", false));
        switchLocation.setChecked(preferences.getBoolean("switch_location", false));
        switchCalls.setChecked(preferences.getBoolean("switch_calls", false));
        switchNotification.setChecked(preferences.getBoolean("switch_notification", false));
        switchSharing.setChecked(preferences.getBoolean("switch_sharing", false));

        // Save state when changed
        switchAlarm.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferences.edit().putBoolean("switch_alarm", isChecked).apply());

        switchLocation.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferences.edit().putBoolean("switch_location", isChecked).apply());

        switchCalls.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferences.edit().putBoolean("switch_calls", isChecked).apply());

        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferences.edit().putBoolean("switch_notification", isChecked).apply());

        switchSharing.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferences.edit().putBoolean("switch_sharing", isChecked).apply());

        // 🌐 Help & Support → open Chrome
        buttonHelpSupport.setOnClickListener(v -> {
            String url = "https://www.who.int/health-topics";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                intent.setPackage("com.android.chrome"); // force Chrome
                startActivity(intent);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))); // fallback
            }
        });


        // ❌ Delete Account
        buttonDeleteAccount.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String uid = user.getUid();
                db.collection("Users").document(uid).delete().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.delete().addOnCompleteListener(authTask -> {
                            if (authTask.isSuccessful()) {
                                Toast.makeText(this, "Account deleted permanently", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(this, "Failed to delete auth: " +
                                        authTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(this, "Failed to delete Firestore data", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    // ✅ Allow other activities to check switch states
    public static boolean isNotificationEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppSettings", MODE_PRIVATE);
        return prefs.getBoolean("switch_notification", false);
    }

    public static boolean isAlarmEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppSettings", MODE_PRIVATE);
        return prefs.getBoolean("switch_alarm", false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
