package com.nibm.medlink_healthcare;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
// 🟢 NEW IMPORT
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateTaskActivity extends AppCompatActivity {

    private EditText etTitle, etSubtasks;
    private Button btnPickDate, btnPickTime, btnSaveTask;
    private TextView tvScheduledDateTime;
    private FirebaseFirestore db;
    private Calendar scheduledCalendar;
    // 🟢 NEW: Add FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        db = FirebaseFirestore.getInstance();
        // 🟢 INITIALIZE AUTH
        mAuth = FirebaseAuth.getInstance();
        scheduledCalendar = Calendar.getInstance();

        // 1. Initialize UI components
        etTitle = findViewById(R.id.etTaskTitle);
        etSubtasks = findViewById(R.id.etSubtasks);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        tvScheduledDateTime = findViewById(R.id.tvScheduledDateTime);

        // 2. Click Listeners
        btnPickDate.setOnClickListener(v -> showDatePicker());
        btnPickTime.setOnClickListener(v -> showTimePicker());
        btnSaveTask.setOnClickListener(v -> saveTaskAndScheduleNotification());

        // Initialize display text
        updateDateTimeDisplay();
    }

    // ... (showDatePicker, showTimePicker, updateDateTimeDisplay methods remain unchanged) ...

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            scheduledCalendar.set(Calendar.YEAR, year);
            scheduledCalendar.set(Calendar.MONTH, month);
            scheduledCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateTimeDisplay();
        }, scheduledCalendar.get(Calendar.YEAR),
                scheduledCalendar.get(Calendar.MONTH),
                scheduledCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            scheduledCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            scheduledCalendar.set(Calendar.MINUTE, minute);
            scheduledCalendar.set(Calendar.SECOND, 0);
            updateDateTimeDisplay();
        }, scheduledCalendar.get(Calendar.HOUR_OF_DAY),
                scheduledCalendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this)).show();
    }

    private void updateDateTimeDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
        String scheduledTimeStr = sdf.format(scheduledCalendar.getTime());
        tvScheduledDateTime.setText("Scheduled for: " + scheduledTimeStr);
    }

    private void saveTaskAndScheduleNotification() {
        String title = etTitle.getText().toString().trim();
        String description = etSubtasks.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if scheduled time is in the future
        long currentTime = System.currentTimeMillis();
        long scheduledTimeMillis = scheduledCalendar.getTimeInMillis();

        if (scheduledTimeMillis <= currentTime) {
            Toast.makeText(this, "Please select a future date and time.", Toast.LENGTH_LONG).show();
            return;
        }

        // 🟢 CRITICAL FIX: GET USER AND CHANGE PATH
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Generate Task ID (using the correct subcollection path to ensure uniqueness)
        String userId = user.getUid();
        String taskId = db.collection("Users").document(userId).collection("Notifications").document().getId();
        Timestamp timestamp = new Timestamp(new Date(scheduledTimeMillis));

        // Note: Task model has (id, title, description, scheduledTime, isCompleted)
        Task task = new Task(taskId, title, description, timestamp, false);

        // 2. Save to user-specific Notifications subcollection
        db.collection("Users") // Collection
                .document(userId) // User Document
                .collection("Notifications") // Subcollection
                .document(taskId) // Task Document ID
                .set(task)
                .addOnSuccessListener(aVoid -> {
                    // 3. Schedule the local notification alarm
                    scheduleAlarm(taskId, title, scheduledTimeMillis);

                    Toast.makeText(this, "Task saved and reminder set!", Toast.LENGTH_LONG).show();
                    finish(); // Close the activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save task: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    // ... (scheduleAlarm method remains unchanged) ...
    private void scheduleAlarm(String taskId, String title, long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);

        // Pass task data to the receiver
        intent.putExtra("task_title", title);
        intent.putExtra("task_id", taskId);

        // Use the task ID to ensure a unique PendingIntent for each alarm
        int requestCode = taskId.hashCode();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode, // Unique request code
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }
}