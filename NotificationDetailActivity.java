package com.nibm.medlink_healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationDetailActivity extends AppCompatActivity {

    private TextView txtDetailTitle, txtDetailMessage, txtDetailTimestamp;
    private CheckBox chkCompleted;
    private ImageView imgTaskStatus, txtBack;

    private String notificationId; // ✅ Store Firestore doc ID
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        txtDetailTitle = findViewById(R.id.txtDetailTitle);
        txtDetailMessage = findViewById(R.id.txtDetailMessage);
        txtDetailTimestamp = findViewById(R.id.txtDetailTimestamp);
        chkCompleted = findViewById(R.id.chkCompleted);
        imgTaskStatus = findViewById(R.id.imgTaskStatus);
        txtBack = findViewById(R.id.txtBack);

        db = FirebaseFirestore.getInstance();

        // Get data from intent
        notificationId = getIntent().getStringExtra("id"); // ✅ Important: Firestore doc ID
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String scheduledTimeStr = getIntent().getStringExtra("scheduledTimeStr");
        boolean completed = getIntent().getBooleanExtra("completed", false);

        txtDetailTitle.setText(title != null ? title : "No title");
        txtDetailMessage.setText(description != null ? description : "");
        txtDetailTimestamp.setText(
                scheduledTimeStr != null && !scheduledTimeStr.isEmpty() ? scheduledTimeStr : "No Date"
        );
        chkCompleted.setChecked(completed);

        // show/hide status icon
        imgTaskStatus.setVisibility(completed ? View.VISIBLE : View.GONE);

        // ✅ Update Firestore when checkbox is clicked
        chkCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String docId = getIntent().getStringExtra("id"); // passed from adapter

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Users")
                        .document(userId)
                        .collection("Notifications")
                        .document(docId)
                        .update("completed", true)
                        .addOnSuccessListener(aVoid -> {
                            // send result back to parent activity
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("docId", docId);
                            setResult(RESULT_OK, resultIntent);
                            finish(); // close detail screen
                        })
                        .addOnFailureListener(e -> {
                            chkCompleted.setChecked(false);
                            Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show();
                        });
            }
        });


        // Back click
        txtBack.setOnClickListener(v -> finish());
    }
}
