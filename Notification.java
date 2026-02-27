package com.nibm.medlink_healthcare;

import com.google.firebase.Timestamp;

public class Notification {
    private String id;
    private String title;
    private String description;
    private Timestamp scheduledTime;
    private boolean completed;
    private boolean isRead;
    private String userId; // optional, if you store per-user

    // Empty constructor required by Firestore
    public Notification() {}

    // Constructor without id (used when creating new)
    public Notification(String title, String description, Timestamp scheduledTime,
                        boolean completed, boolean isRead) {
        this.title = title;
        this.description = description;
        this.scheduledTime = scheduledTime;
        this.completed = completed;
        this.isRead = isRead;
    }

    // Constructor with id
    public Notification(String id, String title, String description, Timestamp scheduledTime,
                        boolean completed, boolean isRead) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.scheduledTime = scheduledTime;
        this.completed = completed;
        this.isRead = isRead;
    }

    // Getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(Timestamp scheduledTime) { this.scheduledTime = scheduledTime; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
