package com.nibm.medlink_healthcare;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateReminderActivity extends AppCompatActivity {

    private EditText etTitle, etMessage, etDate, etTime;
    private Button btnSave, btnCancel;
    private FirebaseFirestore db;
    private Calendar selectedDateTime;
    private Button btnSetAlarm, btnSetNotification;
    private SharedPreferences prefs; // to get user settings
    private static final String PREFS_NAME = "UserSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);

        // views (IDs must match your XML)
        etTitle = findViewById(R.id.etReminderTitle);
        etMessage = findViewById(R.id.etReminderMessage);
        etDate = findViewById(R.id.etReminderDate);
        etTime = findViewById(R.id.etReminderTime);
        btnSave = findViewById(R.id.btnCreateReminder);
        btnCancel = findViewById(R.id.btnCancelReminder);
        // existing initialization...
        btnSetAlarm = findViewById(R.id.btnSetAlarm);
        btnSetNotification = findViewById(R.id.btnSetNotification);

        db = FirebaseFirestore.getInstance();
        selectedDateTime = Calendar.getInstance();

        // Make sure the date/time EditTexts are clickable (your XML already sets clickable="true" and focusable="false")
        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());

        btnSave.setOnClickListener(v -> saveReminder());
        btnCancel.setOnClickListener(v -> finish());
        btnSetAlarm.setOnClickListener(v -> handleAlarmSelection());
        btnSetNotification.setOnClickListener(v -> {
            if (SettingsActivity.isNotificationEnabled(this)) {
                long triggerTime = selectedDateTime.getTimeInMillis();

                if (triggerTime > System.currentTimeMillis()) {
                    scheduleNotification(
                            etTitle.getText().toString(),
                            etMessage.getText().toString(),
                            triggerTime
                    );
                } else {
                    Toast.makeText(this, "Please pick a future date and time", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enable Notifications in Settings first", Toast.LENGTH_LONG).show();
            }
        });
    }
        private void showDatePicker() {
        int y = selectedDateTime.get(Calendar.YEAR);
        int m = selectedDateTime.get(Calendar.MONTH);
        int d = selectedDateTime.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDateTime.set(Calendar.YEAR, year);
            selectedDateTime.set(Calendar.MONTH, month);
            selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateTimeUI();
        }, y, m, d);

        dp.show();
    }

    private void showTimePicker() {
        int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDateTime.get(Calendar.MINUTE);

        TimePickerDialog tp = new TimePickerDialog(this, (view, h, min) -> {
            selectedDateTime.set(Calendar.HOUR_OF_DAY, h);
            selectedDateTime.set(Calendar.MINUTE, min);
            selectedDateTime.set(Calendar.SECOND, 0);
            updateDateTimeUI();
        }, hour, minute, false); // false => 12-hour format; change to true if you prefer 24h
        tp.show();
    }

    private void updateDateTimeUI() {
        SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        etDate.setText(dateFmt.format(selectedDateTime.getTime()));
        etTime.setText(timeFmt.format(selectedDateTime.getTime()));
    }

    private void saveReminder() {
        String title = etTitle.getText().toString().trim();
        String description = etMessage.getText().toString().trim();
        String dateText = etDate.getText().toString().trim();
        String timeText = etTime.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dateText.isEmpty() || timeText.isEmpty()) {
            Toast.makeText(this, "Please pick date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        long millis = selectedDateTime.getTimeInMillis();
        if (millis <= System.currentTimeMillis()) {
            // optional: allow saving past reminders but warn
            Toast.makeText(this, "Please pick a future date/time", Toast.LENGTH_SHORT).show();
            return;
        }

        // create doc id (consistent even offline)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user != null ? user.getUid() : null;

        final String docId;
        if (userId != null) {
            docId = db.collection("Users").document(userId).collection("Notifications").document().getId();
        } else {
            docId = db.collection("notifications").document().getId();
        }

        // build payload to match your Firestore fields
        Map<String, Object> reminder = new HashMap<>();
        reminder.put("id", docId);
        reminder.put("title", title);
        reminder.put("description", description);
        reminder.put("scheduledTime", new Timestamp(selectedDateTime.getTime()));
        reminder.put("completed", false);
        reminder.put("isRead", false);

        if (userId != null) {
            db.collection("Users")
                    .document(userId)
                    .collection("Notifications")
                    .document(docId)
                    .set(reminder)
                    .addOnSuccessListener(aVoid -> {
                        scheduleAlarm(docId, title, description, millis);
                        Toast.makeText(CreateReminderActivity.this, "Reminder saved", Toast.LENGTH_SHORT).show();
                        // return ok so calling activity can refresh
                        setResult(RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(CreateReminderActivity.this, "Save failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
        } else {
            db.collection("notifications")
                    .document(docId)
                    .set(reminder)
                    .addOnSuccessListener(aVoid -> {
                        scheduleAlarm(docId, title, description, millis);
                        Toast.makeText(CreateReminderActivity.this, "Reminder saved", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(CreateReminderActivity.this, "Save failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleNotification(String title, String message, long timeInMillis) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // ✅ Check exact-alarm permission first (Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                Toast.makeText(this, "Please allow exact alarms to enable reminders", Toast.LENGTH_LONG).show();
                return; // stop here until user grants permission
            }
        }

        // ✅ Proceed only if we have permission
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            Toast.makeText(this, "Notification scheduled", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleAlarm(String id, String title, String message, long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("description", message);
        intent.putExtra("notificationId", id);

        int requestCode = id.hashCode();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            // use setExact if you want precise delivery
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }

    private void handleAlarmSelection() {
        if (SettingsActivity.isAlarmEnabled(this)) {
            Toast.makeText(this, "Alarm will ring at the set time", Toast.LENGTH_SHORT).show();
            // logic to trigger alarm based on selected reminder time
        } else {
            Toast.makeText(this, "Please enable Alarm in Settings first", Toast.LENGTH_LONG).show();
        }
    }

    private void handleNotificationSelection() {
        if (SettingsActivity.isNotificationEnabled(this)) {
            Toast.makeText(this, "Notification will appear at the set time", Toast.LENGTH_SHORT).show();
            // logic to trigger notification based on selected reminder time
        } else {
            Toast.makeText(this, "Please enable Notifications in Settings first", Toast.LENGTH_LONG).show();
        }
    }

}
