package com.nibm.medlink_healthcare;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreHelper {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void saveAppointment(Appointment appointment, OnCompleteListener listener) {
        db.collection("appointments")
                .document()
                .set(appointment)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    public interface OnCompleteListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
