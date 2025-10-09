package com.nibm.medlink_healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private EditText etFullName, etAddress, etNIC, etContact, etEmail;
    private Button btnEdit, btnSave, btnChangePhoto, btnRemovePhoto;
    private CircleImageView imgProfile;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 100;

    private String base64Image = ""; // store image as Base64 string

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etFullName = findViewById(R.id.etFullName);
        etAddress = findViewById(R.id.etAddress);
        etNIC = findViewById(R.id.etNIC);
        etContact = findViewById(R.id.etContact);
        etEmail = findViewById(R.id.etEmail);

        imgProfile = findViewById(R.id.imgProfile);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        btnRemovePhoto = findViewById(R.id.btnRemovePhoto);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);

        // Load profile data when activity starts
        loadProfile();

        btnEdit.setOnClickListener(v -> enableEditing(true));
        btnSave.setOnClickListener(v -> saveProfile());
        btnChangePhoto.setOnClickListener(v -> openGallery());
        btnRemovePhoto.setOnClickListener(v -> removeProfilePhoto());
    }

    private void enableEditing(boolean enable) {
        etFullName.setEnabled(enable);
        etAddress.setEnabled(enable);
        etNIC.setEnabled(enable);
        etContact.setEnabled(enable);
        etEmail.setEnabled(enable);
    }

    private void saveProfile() {
        String userId = mAuth.getCurrentUser().getUid();

        Map<String, Object> profile = new HashMap<>();
        profile.put("fullName", etFullName.getText().toString().trim());
        profile.put("address", etAddress.getText().toString().trim());
        profile.put("nic", etNIC.getText().toString().trim());
        profile.put("contact", etContact.getText().toString().trim());
        profile.put("email", etEmail.getText().toString().trim());
        profile.put("profileImage", base64Image); // ✅ save Base64 string

        DocumentReference docRef = db.collection("Users").document(userId);
        docRef.set(profile, SetOptions.merge()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                enableEditing(false);
            } else {
                Toast.makeText(ProfileActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadProfile() {
        String userId = mAuth.getCurrentUser().getUid();

        DocumentReference docRef = db.collection("Users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    String fullName = task.getResult().getString("fullName");
                    String address = task.getResult().getString("address");
                    String nic = task.getResult().getString("nic");
                    String contact = task.getResult().getString("contact");
                    String email = task.getResult().getString("email");
                    String encodedImage = task.getResult().getString("profileImage");

                    etFullName.setText(fullName);
                    etAddress.setText(address);
                    etNIC.setText(nic);
                    etContact.setText(contact);
                    etEmail.setText(email);

                    if (encodedImage != null && !encodedImage.isEmpty()) {
                        byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                        imgProfile.setImageBitmap(bitmap);
                        base64Image = encodedImage; // keep for saving again
                    } else {
                        imgProfile.setImageResource(R.drawable.ic_profile);
                    }

                    enableEditing(false);
                } else {
                    Toast.makeText(ProfileActivity.this, "No profile found. Please create one.", Toast.LENGTH_SHORT).show();
                    enableEditing(true);
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgProfile.setImageBitmap(bitmap);

                // convert bitmap → Base64 string
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); // 50% quality to reduce size
                byte[] imageBytes = baos.toByteArray();
                base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void removeProfilePhoto() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("Users").document(userId)
                .update("profileImage", "")
                .addOnSuccessListener(unused -> {
                    imgProfile.setImageResource(R.drawable.ic_profile);
                    base64Image = "";
                    Toast.makeText(ProfileActivity.this, "Profile photo removed", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(ProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
